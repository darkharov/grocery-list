package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CustomProductListMapper @Inject constructor() {

    fun toDomain(entity: CustomProductListEntity): ProductList.Custom =
        ProductList.Custom(
            id = entity.id ?: throw IllegalStateException("CustomProductListEntity must be queried from DB"),
            title = entity.title,
        )

    fun listToDomain(entity: List<CustomProductListEntity>): List<ProductList.Custom> =
        entity.map(::toDomain)

    fun toData(createParams: ProductList.Custom.CreateParams): CustomProductListEntity=
        CustomProductListEntity(
            id = null,
            title = createParams.title,
        )
}
