package app.grocery.list.custom.product.lists.picker.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal sealed class ProductListPickerDialogProps {

    @Immutable
    data class CustomListDeletionConfirmation(
        val id: String,
        val title: String,
    ) : ProductListPickerDialogProps()
}

internal class ProductListPickerDialogMocks : PreviewParameterProvider<ProductListPickerDialogProps> {

    override val values = sequenceOf<ProductListPickerDialogProps>(
        ProductListPickerDialogProps.CustomListDeletionConfirmation(
            id = "1",
            title = "My List",
        )
    )
}
