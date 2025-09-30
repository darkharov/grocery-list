package app.grocery.list.product.list.actions.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.elements.dialog.AppBaseDialog
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.dialog.AppTwoOptionsDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
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
