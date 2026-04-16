package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListIdMapper @Inject constructor() {

    fun toData(productListId: ProductList.Id): Int? =
        when (productListId) {
            is ProductList.Id.Custom -> {
                productListId.backingId
            }
            is ProductList.Id.Default -> {
                null
            }
        }

    fun toDomain(customProductListId: Int?): ProductList.Id =
        if (customProductListId != null) {
            ProductList.Id.Custom(backingId = customProductListId)
        } else {
            ProductList.Id.Default
        }
}
