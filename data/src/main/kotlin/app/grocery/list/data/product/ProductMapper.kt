package app.grocery.list.data.product

import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordMapper
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductMapper @Inject constructor(
    private val emojiAndKeywordMapper: EmojiAndKeywordMapper,
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
            customListId = (product.productListId as? ProductList.Id.Custom)?.backingId,
        )

    fun toDomain(entity: ProductEntity): Product {
        val customListId = entity.customListId
        return Product(
            id = entity.id ?: throw IllegalStateException("ProductEntity must be queried from DB"),
            title = entity.title,
            emojiAndKeyword = emojiAndKeywordMapper.toDomain(
                projection = entity.emojiAndKeyword,
            ),
            enabled = entity.enabled,
            categoryId = entity.nonFkCategoryId,
            productListId = if (customListId != null) {
                ProductList.Id.Custom(backingId = customListId)
            } else {
                ProductList.Id.Default
            },
        )
    }
}
