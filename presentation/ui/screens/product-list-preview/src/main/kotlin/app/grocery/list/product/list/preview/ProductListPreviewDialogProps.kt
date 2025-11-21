package app.grocery.list.product.list.preview

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps

@Immutable
sealed class ProductListPreviewDialogProps {

    @Immutable
    class ConfirmPastedProductsWrapper(
        val dialog: ConfirmPastedListDialogProps,
    ) : ProductListPreviewDialogProps()
}
