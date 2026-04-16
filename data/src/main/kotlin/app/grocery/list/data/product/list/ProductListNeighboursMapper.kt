package app.grocery.list.data.product.list

import app.grocery.list.data.product.list.summary.ProductListWithCountersMapper
import app.grocery.list.data.product.list.summary.ProductListWithCountersView
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListNeighboursMapper @Inject constructor(
    private val productListWithCountersMapper: ProductListWithCountersMapper,
) {
    fun toDomain(items: Array<ProductListWithCountersView?>): ProductList.Neighbours {

        val trailing = items[0]
        val leading = items[1]

        return if (
            trailing?.customListId ==
            leading?.customListId
        ) {
            if (trailing?.customListId == null) {
                ProductList.Neighbours(
                    trailing = trailing?.let(productListWithCountersMapper::toDomain),
                    leading = null
                )
            } else {
                ProductList.Neighbours(
                    trailing = null,
                    leading = leading?.let(productListWithCountersMapper::toDomain),
                )
            }
        } else {
            ProductList.Neighbours(
                trailing = trailing
                    ?.let(productListWithCountersMapper::toDomain),
                leading = leading
                    ?.let(productListWithCountersMapper::toDomain),
            )
        }
    }
}
