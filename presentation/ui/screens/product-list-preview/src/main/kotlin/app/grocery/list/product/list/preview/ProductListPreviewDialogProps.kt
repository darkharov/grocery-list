package app.grocery.list.product.list.preview

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListMock

@Immutable
sealed class ProductListPreviewDialogProps {

    @Immutable
    object HowToEditProducts : ProductListPreviewDialogProps()

    @Immutable
    class ConfirmPastedProductsWrapper(
        val dialog: ConfirmPastedListDialogProps,
    ) : ProductListPreviewDialogProps()
}

internal class ProductListPreviewDialogMocks : PreviewParameterProvider<ProductListPreviewDialogProps> {

    override val values = sequenceOf(
        ProductListPreviewDialogProps.HowToEditProducts,
        ProductListPreviewDialogProps.ConfirmPastedProductsWrapper(
            dialog = ConfirmPastedListMock.prototype,
        ),
    )
}
