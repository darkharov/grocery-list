package app.grocery.list.custom.product.lists.picker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemMocks
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemProps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
internal class ProductListPickerProps(
    val items: ImmutableList<ProductListPickerItemProps>,
    val question: Question?,
) {
    @Immutable
    sealed class Question {

        @Immutable
        data object HowToRenameOrDeleteCustomList : Question()
    }
}

internal class ProductListPickerMocks : PreviewParameterProvider<ProductListPickerProps?> {
    override val values = sequenceOf(
        null,
        ProductListPickerProps(
            items = ProductListPickerItemMocks().values.toPersistentList(),
            question = null,
        ),
    )
}
