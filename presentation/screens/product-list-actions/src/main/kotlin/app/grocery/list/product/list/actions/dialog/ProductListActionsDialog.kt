package app.grocery.list.product.list.actions.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.drawGradient
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.elements.dialog.APP_DIALOG_PADDING
import app.grocery.list.commons.compose.elements.dialog.AppBaseDialog
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.dialog.AppTwoOptionsDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.ProductListActionsCallbacks
import app.grocery.list.product.list.actions.ProductListActionsCallbacksMock
import app.grocery.list.product.list.actions.R

@Composable
internal fun ProductListActionsOptionalDialog(
    dialog: ProductListActionsDialogProps?,
    callbacks: ProductListActionsCallbacks,
) {
    if (dialog != null) {
        ProductListActionsDialog(
            dialog = dialog,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun ProductListActionsDialog(
    dialog: ProductListActionsDialogProps,
    callbacks: ProductListActionsCallbacks,
) {
    when (dialog) {
        is ProductListActionsDialogProps.ConfirmClearList -> {
            AppTwoOptionsDialog(
                icon = rememberVectorPainter(AppIcons.delete),
                text = StringValue.ResId(R.string.clear_product_list_confirmation),
                firstOption = StringValue.ResId(R.string.delete),
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
        is ProductListActionsDialogProps.CopiedProductListNotFound -> {
            AppSimpleDialog(
                icon = rememberVectorPainter(AppIcons.paste),
                text = StringValue.ResId(R.string.copied_product_list_not_found),
                onConfirm = {
                    callbacks.onDialogDismiss()
                },
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialogProps.HowToPutPastedProducts -> {
            AppTwoOptionsDialog(
                icon = rememberVectorPainter(AppIcons.paste),
                text = StringValue.ResId(R.string.add_or_replace_copied_products),
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
        is ProductListActionsDialogProps.ProductSuccessfullyAdded -> {
            AppSimpleDialog(
                icon = rememberVectorPainter(AppIcons.done),
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
        is ProductListActionsDialogProps.SublistToSharePicker -> {
            AppBaseDialog(
                icon = rememberVectorPainter(AppIcons.share),
                text = StringValue.ResId(R.string.sublist_to_share_dialog_title),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
            ) {
                AppTextButton(
                    props = AppTextButtonProps.TextOnly(
                        text = StringValue.StringWrapper(
                            stringResource(
                                R.string.pattern_disabled_to_send,
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
                                R.string.pattern_enabled_to_send,
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
        is ProductListActionsDialogProps.EnableAllAndStartShopping -> {
            AppSimpleDialog(
                icon = rememberVectorPainter(AppIcons.toggleOff),
                text = StringValue.ResId(
                    resId = R.string.error_no_enabled_products_to_start_shopping,
                    arguments = listOf(dialog.totalProductCount),
                ),
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
                confirmButtonText = StringValue.ResId(R.string.enable_all_and_start),
                onConfirm = {
                    callbacks.onEnableAllAndStartShopping()
                },
                onCancel = {
                    callbacks.onDialogDismiss()
                },
            )
        }
        is ProductListActionsDialogProps.ConfirmHandTypedList -> {
            AppBaseDialog(
                icon = rememberVectorPainter(AppIcons.paste),
                text = StringValue.PluralResId(
                    resId = R.plurals.pattern_products_found,
                    count = dialog.numberOfFoundProducts,
                    useCountAsArgument = true,
                ),
                textStyle = LocalAppTypography.current.dialogTitle,
                onDismiss = {
                    callbacks.onDialogDismiss()
                },
                additionalContent = {
                    val offset = 48.dp
                    val state = rememberScrollState()
                    Text(
                        text = dialog.itemTitles,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .heightIn(max = 4 * offset)
                            .drawGradient(
                                direction = AppGradientDirection.Upward,
                                color = MaterialTheme.colorScheme.surface,
                                height = offset,
                                visible = state.canScrollForward,
                            )
                            .drawGradient(
                                direction = AppGradientDirection.Downward,
                                color = MaterialTheme.colorScheme.surface,
                                height = offset,
                                visible = state.canScrollBackward,
                            )
                            .verticalScroll(state)
                            .padding(horizontal = 40.dp),
                    )
                    Spacer(
                        modifier = Modifier
                            .padding(4.dp),
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(APP_DIALOG_PADDING),
                    ) {
                        AppTextButton(
                            props = AppTextButtonProps.TextOnly(
                                text = StringValue.ResId(android.R.string.cancel),
                            ),
                            onClick = {
                                callbacks.onDialogDismiss()
                            },
                        )
                        AppTextButton(
                            props = AppTextButtonProps.TextOnly(
                                text = StringValue.ResId(R.string.add),
                            ),
                            onClick = {
                                callbacks.onPasteHandTypedProducts(dialog.productList)
                            },
                        )
                    }
                },
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun ProductListActionsDialogPreview(
    @PreviewParameter(
        provider = ProductListActionsDialogMocks::class,
    )
    dialog: ProductListActionsDialogProps,
) {
    GroceryListTheme {
        ProductListActionsDialog(
            dialog = dialog,
            callbacks = ProductListActionsCallbacksMock,
        )
    }
}
