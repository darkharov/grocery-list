package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CustomProductListMapper @Inject constructor() {

    fun toData(details: ProductList.PutParams): CustomProductListEntity =
        CustomProductListEntity(
            id = details.customListId?.backingId ?: 0,
            title = details.title,
            colorScheme = details.colorScheme.ordinal,
        )

    fun toDomain(entity: CustomProductListEntity): ProductList =
        ProductList(
            id = ProductList.Id.Custom(entity.id),
            title = entity.title,
            colorScheme = ColorScheme.entries[entity.colorScheme],
        )
}
