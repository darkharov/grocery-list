package app.grocery.list.product.list.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.elements.app.button.WideAppButton
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
internal fun ProductListActionsScreen(
    viewModel: ProductListActionsViewModel = hiltViewModel(),
    delegate: ProductListActionsDelegate,
) {
    EventConsumer(
        key = viewModel,
        lifecycleState = Lifecycle.State.RESUMED,
        events = viewModel.events(),
    ) { event ->
        when (event) {
            is Event.OnExitFromApp -> {
                delegate.onExitFromApp()
            }
            is Event.OnShare -> {
                delegate.share(event.products)
            }
            is Event.OnStartShopping -> {
                delegate.onStartShopping()
            }
        }
    }
    val dialog by viewModel.dialog().collectAsState()
    val props by viewModel.props.collectAsState()
    ProductListActionsScreen(
        props = props,
        dialog = dialog,
        callbacks = viewModel,
    )
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
    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize(),
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
                state = AppButtonProps.State.enabled(props.numberOfProducts > 0)
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
                state = AppButtonProps.State.enabled(props.numberOfProducts > 0),
            ),
            onClick = {
                callbacks.onStartShopping()
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text =
                    if (props.numberOfProducts == 0) {
                        stringResource(R.string.share_this_list)
                    } else {
                        stringResource(
                            R.string.share_this_list_with_counter,
                            props.numberOfProducts,
                        )
                    },
                drawableEndId = R.drawable.ic_share,
                state = when {
                    props.numberOfProducts == 0 -> {
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
                        val text = clipEntry?.clipData?.getItemAt(0)?.text
                        if (!(text.isNullOrBlank())) {
                            callbacks.onPaste(text.toString())
                        }
                    }
                }
            },
        )
        WideAppButton(
            props = AppButtonProps.Custom(
                text = stringResource(
                    if (props.numberOfProducts > 0) {
                        R.string.save_and_exit
                    } else {
                        R.string.exit
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

@Composable
private fun Dialog(
    dialog: ProductListActionsDialog,
    callbacks: ProductListActionsCallbacks,
) {
    when (dialog) {
        is ProductListActionsDialog.ConfirmClearList -> {
            AppSimpleDialog(
                icon = painterResource(R.drawable.ic_bin_outline),
                text = StringValue.ResId(
                    R.string.clear_product_list_confirmation,
                ),
                confirmButtonText = StringValue.ResId(
                    R.string.delete,
                ),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
                onConfirm = {
                    callbacks.onClearListConfirmed()
                },
                onCancel = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialog.CopiedProductListNotFound -> {
            AppSimpleDialog(
                text = StringValue.ResId(
                    R.string.copied_product_list_not_found,
                ),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialog.HowToPutPastedProducts -> {
            AppTwoOptionsDialog(
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
                    loadingListToShare = false,
                    numberOfProducts = 0,
                ),
            )
        }
    }
}
