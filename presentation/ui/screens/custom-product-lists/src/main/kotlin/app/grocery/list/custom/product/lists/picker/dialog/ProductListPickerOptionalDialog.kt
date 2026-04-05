package app.grocery.list.custom.product.lists.picker.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.dialog.AppHowToEditListItemsDialog
import app.grocery.list.commons.compose.elements.dialog.AppTwoOptionsDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.custom.product.lists.R

@Composable
internal fun ProductListPickerOptionalDialog(
    props: ProductListPickerDialogProps?,
    callbacks: ProductListPickerDialogCallbacks,
) {
    when (props) {
        is ProductListPickerDialogProps.CustomListDeletionConfirmation -> {
            AppTwoOptionsDialog(
                icon = rememberVectorPainter(AppIcons.delete),
                title = StringValue.StringWrapper(props.title),
                text = StringValue.ResId(R.string.delete_product_list_confirmation),
                onDismiss = {
                    callbacks.onDeletionRejected(props.id)
                },
                firstOption = StringValue.ResId(R.string.delete),
                isFirstOptionSensitive = true,
                onFirstOption = {
                    callbacks.onDeletionConfirmed(props.id)
                },
            )
        }
        is ProductListPickerDialogProps.HowToRenameOrDeleteCustomList -> {
            AppHowToEditListItemsDialog(
                callbacks = callbacks,
                additionalText = StringValue.ResId(R.string.default_list_is_uneditable_notice),
            )
        }
        null -> {
            // nothing to show
        }
    }
}

@Composable
@PreviewLightDark
private fun ProductListPickerOptionalDialogPreview(
    @PreviewParameter(
        provider = ProductListPickerDialogMocks::class,
    )
    props: ProductListPickerDialogProps,
) {
    GroceryListTheme {
        ProductListPickerOptionalDialog(
            props = props,
            callbacks = ProductListPickerDialogCallbacksMock,
        )
    }
}
