package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.elements.button.AppButtonProps
import app.grocery.list.commons.compose.elements.button.WideAppButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.elements.dialog.AppBaseDialog
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.dialog.AppTwoOptionsDialog
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
data object ProductListActions

fun NavGraphBuilder.productListActionsScreen(
    delegate: ProductListActionsDelegate,
) {
    composable<ProductListActions> {
        ProductListActionsScreen(
            delegate = delegate,
        )
    }
}

@Composable
private fun ProductListActionsScreen(
    delegate: ProductListActionsDelegate,
) {
    val viewModel: ProductListActionsViewModel = hiltViewModel()
    val dialog by viewModel.dialog().collectAsState()
    val props by viewModel.props.collectAsState()
    EventsConsumer(
        viewModel = viewModel,
        delegate = delegate,
    )
    ProductListActionsScreen(
        props = props,
        dialog = dialog,
        callbacks = viewModel,
    )
}

@Composable
private fun EventsConsumer(
    viewModel: ProductListActionsViewModel,
    delegate: ProductListActionsDelegate,
) {
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            is Event.OnExitFromApp -> {
                delegate.exitFromApp()
            }
            is Event.OnShare -> {
                delegate.share(event.products)
            }
            is Event.OnStartShopping -> {
                delegate.startShopping()
            }
        }
    }
}

@Composable
private fun ProductListActionsScreen(
    props: ProductListActionsProps?,
    dialog: ProductListActionsDialog?,
    callbacks: ProductListActionsCallbacks,
    modifier: Modifier = Modifier,
) {
    if (props == null) {
        AppPreloader()
    } else {
        Content(props, callbacks, modifier)
    }
    if (dialog != null) {
        Dialog(dialog, callbacks)
    }
}

@Composable
private fun Content(
    props: ProductListActionsProps,
    callbacks: ProductListActionsCallbacks,
    modifier: Modifier,
) {
    val scrollState = rememberScrollState()
    ScrollableContentWithShadows(scrollState) {
        Column(
            modifier = modifier
                .padding(horizontal = 12.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
            WideAppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.clear_list),
                    background = AppButtonProps.Background.Negative,
                    drawableEndId = R.drawable.ic_bin_outline,
                    state = AppButtonProps.State.enabled(!(props.productListEmpty))
                ),
                onClick = {
                    callbacks.onGoToClearListConfirmation()
                },
            )
            WideAppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.i_am_at_shop),
                    background = AppButtonProps.Background.Positive,
                    drawableEndId = R.drawable.ic_cart,
                    state = AppButtonProps.State.enabled(!(props.productListEmpty)),
                ),
                onClick = {
                    callbacks.onStartShopping()
                },
            )
            WideAppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.share_current_list),
                    drawableEndId = R.drawable.ic_share,
                    state = when {
                        props.productListEmpty -> {
                            AppButtonProps.State.Disabled
                        }
                        props.loadingListToShare -> {
                            AppButtonProps.State.DisabledWithProgressBar
                        }
                        else -> {
                            AppButtonProps.State.Enabled
                        }
                    }
                ),
                onClick = {
                    callbacks.onShare()
                },
            )
            val clipboard = LocalClipboard.current
            val clipboardScope = rememberCoroutineScope()
            WideAppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(R.string.paste_copied_list),
                    drawableEndId = R.drawable.ic_paste,
                ),
                onClick = {
                    clipboardScope.launch(Dispatchers.IO) {
                        val clipEntry = clipboard.getClipEntry()
                        withContext(Dispatchers.Main) {
                            val text = clipEntry?.clipData?.getItemAt(0)?.text?.toString().orEmpty()
                            callbacks.onPaste(text = text)
                        }
                    }
                },
            )
            WideAppButton(
                props = AppButtonProps.Custom(
                    text = stringResource(
                        if (props.productListEmpty) {
                            R.string.exit
                        } else {
                            R.string.save_and_exit
                        }
                    ),
                    drawableEndId = R.drawable.ic_exit,
                ),
                onClick = {
                    callbacks.onExitFromApp()
                },
            )
            Spacer(
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

@Composable
private fun Dialog(
    dialog: ProductListActionsDialog,
    callbacks: ProductListActionsCallbacks,
) {
    when (dialog) {
        is ProductListActionsDialog.ConfirmClearList -> {
            AppTwoOptionsDialog(
                icon = painterResource(R.drawable.ic_bin_outline),
                text = StringValue.ResId(
                    R.string.clear_product_list_confirmation,
                ),
                firstOption = StringValue.ResId(
                    R.string.delete,
                ),
                isFirstOptionSensitive = true,
                onFirstOption = {
                    callbacks.onClearListConfirmed()
                },
                onSecondOption = {
                    callbacks.onDialogDismiss()
                },
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialog.CopiedProductListNotFound -> {
            AppSimpleDialog(
                icon = painterResource(R.drawable.ic_paste),
                text = StringValue.ResId(
                    R.string.copied_product_list_not_found,
                ),
                onConfirm = {
                    callbacks.onDialogDismiss()
                },
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialog.HowToPutPastedProducts -> {
            AppTwoOptionsDialog(
                icon = painterResource(R.drawable.ic_paste),
                text = StringValue.ResId(
                    resId = R.string.add_or_replace_copied_products,
                ),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
                firstOption = StringValue.ResId(R.string.replace),
                onFirstOption = {
                    callbacks.onReplaceProductsBy(dialog.productList)
                },
                secondOption = StringValue.ResId(R.string.add),
                onSecondOption = {
                    callbacks.onAddProducts(dialog.productList)
                },
                isFirstOptionSensitive = true,
            )
        }
        is ProductListActionsDialog.ProductSuccessfullyAdded -> {
            AppSimpleDialog(
                icon = painterResource(R.drawable.ic_done),
                text = StringValue.PluralResId(
                    R.plurals.products_successfully_added,
                    dialog.count,
                    useCountAsArgument = true,
                ),
                onConfirm = {
                    callbacks.onDialogDismiss()
                },
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialog.SublistToSharePicker -> {
            AppBaseDialog(
                icon = painterResource(R.drawable.ic_share),
                text = StringValue.ResId(
                    resId = R.string.sublist_to_share_dialog_title,
                ),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            ) {
                AppTextButton(
                    props = AppTextButtonProps.TextOnly(
                        text = StringValue.StringWrapper(
                            stringResource(
                                R.string.pattern_disabled,
                                dialog.disabledItemsCount,
                            )
                        )
                    ),
                    onClick = {
                        callbacks.onShareDisabledOnly(dialog)
                    },
                    modifier = Modifier,
                )
                AppTextButton(
                    props = AppTextButtonProps.TextOnly(
                        text = StringValue.StringWrapper(
                            stringResource(
                                R.string.pattern_enabled,
                                dialog.enabledItemsCount,
                            )
                        )
                    ),
                    onClick = {
                        callbacks.onShareEnabledOnly(dialog)
                    },
                    modifier = Modifier,
                )
                AppTextButton(
                    props = AppTextButtonProps.TextOnly(
                        text = StringValue.StringWrapper(
                            stringResource(
                                R.string.pattern_all,
                                dialog.productListSize,
                            )
                        )
                    ),
                    onClick = {
                        callbacks.onShareAll(dialog)
                    },
                    modifier = Modifier,
                )
                Spacer(
                    modifier = Modifier
                        .height(8.dp),
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun ProductListActionsScreenWithDialogPreview(
    @PreviewParameter(
        provider = ProductListActionsDialogMocks::class,
    )
    dialog: ProductListActionsDialog?,
) {
    GroceryListTheme {
        Scaffold { padding ->
            ProductListActionsScreen(
                dialog = dialog,
                callbacks = ProductListActionsCallbacksMock,
                modifier = Modifier
                    .padding(padding),
                props = ProductListActionsProps(
                    productListEmpty = false,
                    loadingListToShare = false,
                ),
            )
        }
    }
}
