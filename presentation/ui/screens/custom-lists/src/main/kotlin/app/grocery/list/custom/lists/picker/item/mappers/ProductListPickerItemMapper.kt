package app.grocery.list.custom.lists.picker.item.mappers

import app.grocery.list.custom.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.domain.product.list.ProductList
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class ProductListPickerItemMapper @AssistedInject constructor(
    private val demoColorsMapper: DemoColorMapper,
    private val productListIdMapper: ProductListIdMapper,
    @Assisted
    private val last: Boolean,
) {
    fun toPresentation(summary: ProductList.Summary): ProductListPickerItemProps {
        val productList = summary.productList
        val id = productList.id
        return ProductListPickerItemProps(
            id = productListIdMapper.toPresentation(
                id = productList.id,
            ),
            title = productList.title + if (summary.size > 0) {
                " (${summary.size})"
            } else {
                ""
            },
            stub = summary.formattedStub.ifBlank { null },
            demoColors = demoColorsMapper.toPresentation(
                colorScheme = summary.productList.colorScheme,
            ),
            protected = (id is ProductList.Id.Default),
            selected = summary.isSelected,
            hasDivider = !(last),
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(last: Boolean): ProductListPickerItemMapper
    }
}
