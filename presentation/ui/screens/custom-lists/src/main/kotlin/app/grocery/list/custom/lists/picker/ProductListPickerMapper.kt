package app.grocery.list.custom.lists.picker

import app.grocery.list.custom.lists.picker.item.mappers.ProductListPickerItemMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListPickerMapper @Inject constructor(
    private val itemMapperFactory: ProductListPickerItemMapper.Factory,
) {
    fun toPresentation(items: List<ProductList.Summary>): ProductListPickerProps =
        ProductListPickerProps(
            items = items
                .mapIndexed { index, item ->
                    itemMapperFactory
                        .create(last = (index == items.lastIndex))
                        .toPresentation(item)
                }
                .toImmutableList(),
        )
}
