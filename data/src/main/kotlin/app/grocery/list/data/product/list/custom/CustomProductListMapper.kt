package app.grocery.list.data.product.list.custom

import app.grocery.list.data.product.list.ProductListIdMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CustomProductListMapper @Inject constructor(
    private val idMapper: ProductListIdMapper,
) {
    fun toData(params: ProductList.PutParams) =
        CustomProductListEntity(
            id = params
                .customListId
                ?.let(idMapper::toData)
                ?: 0,
            title = params.title,
            colorScheme = params.colorScheme.ordinal,
        )

    fun toDomain(entity: CustomProductListEntity): ProductList =
        ProductList(
            id = idMapper.toDomain(entity.id),
            title = entity.title,
            colorScheme = ColorScheme.entries[entity.colorScheme],
        )
}
