package app.grocery.list.custom.product.lists.picker

import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListPickerItemMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductListPickerMapper @Inject constructor(
    private val itemMapper: ProductListPickerItemMapper,
) {
    fun toPresentation(params: Params): ProductListPickerProps =
        ProductListPickerProps(
            items = params
                .items
                .filter { item ->
                    params.idsOfExcludedOnes.none { it == item.productList.id }
                }
                .map { item ->
                    itemMapper.toPresentation(item)
                }
                .toImmutableList(),
        )

    data class Params(
        val items: List<ProductList.Summary>,
        val idsOfExcludedOnes: Set<ProductList.Id>,
    )
}
