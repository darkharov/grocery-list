package app.grocery.list.custom.lists.picker.item.mappers

import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListIdMapper @Inject constructor() {

    fun toPresentation(id: ProductList.Id) =
        when (id) {
            is ProductList.Id.Custom -> {
                id.backingId.toString()
            }
            is ProductList.Id.Default -> {
                DEFAULT_LIST_ID
            }
        }

    fun toDomain(itemId: String) =
        when (itemId) {
            DEFAULT_LIST_ID -> {
                ProductList.Id.Default
            }
            else -> {
                ProductList.Id.Custom(backingId = itemId.toInt())
            }
        }

    private companion object {
        private const val DEFAULT_LIST_ID = "app.grocery.list.custom.lists.picker.DEFAULT_LIST_ID"
    }
}
