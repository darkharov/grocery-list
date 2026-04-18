package app.grocery.list.product.list.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.dialog.AppHowToEditListItemsDialog
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialog
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
internal fun ProductListPreviewDialog(
    props: ProductListPreviewDialogProps?,
    callbacks: ProductListPreviewCallbacks,
) {
    when (props) {
        is ProductListPreviewDialogProps.ConfirmPastedProductsWrapper -> {
            ConfirmPastedListDialog(
                props = props.dialog,
                callbacks = callbacks,
            )
        }
        is ProductListPreviewDialogProps.HowToEditProducts -> {
            AppHowToEditListItemsDialog(
                callbacks = callbacks,
            )
        }
        null -> {
            // nothing to show
        }
    }
}

@PreviewLightDark
@Composable
private fun ProductListPreviewDialogPreview(
    @PreviewParameter(
        provider = ProductListPreviewDialogMocks::class,
    )
    props: ProductListPreviewDialogProps,
) {
    GroceryListTheme {
        ProductListPreviewDialog(
            props = props,
            callbacks = ProductListPreviewCallbacksMock,
        )
    }
}
