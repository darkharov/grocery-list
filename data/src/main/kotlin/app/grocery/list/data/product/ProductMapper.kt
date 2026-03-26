package app.grocery.list.data.product

import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordMapper
import app.grocery.list.data.product.list.CustomProductListIdMapper
import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductMapper @Inject constructor(
    private val emojiAndKeywordMapper: EmojiAndKeywordMapper,
    private val customProductListIdMapper: CustomProductListIdMapper,
) {
    fun toData(product: Product): ProductEntity =
        ProductEntity(
            id = product.id.takeIf { it != 0 },
            title = product.title,
            emojiAndKeyword = emojiAndKeywordMapper.toData(
                emojiAndKeyword = product.emojiAndKeyword,
            ),
            enabled = product.enabled,
            nonFkCategoryId = product.categoryId,
            customListId = customProductListIdMapper.toData(
                productListId = product.productListId,
            ),
        )

    fun toDomain(entity: ProductEntity) =
        Product(
            id = entity.id ?: throw IllegalStateException("ProductEntity must be queried from DB"),
            title = entity.title,
            emojiAndKeyword = emojiAndKeywordMapper.toDomain(
                projection = entity.emojiAndKeyword,
            ),
            enabled = entity.enabled,
            categoryId = entity.nonFkCategoryId,
            productListId = customProductListIdMapper.toDomain(
                customProductListId = entity.customListId,
            ),
        )
}
