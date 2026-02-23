package app.grocery.list.data.product.list

import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListStubMapper @Inject constructor(
    private val emojiAndKeywordMapper: EmojiAndKeywordMapper,
) {
    fun toDomain(query: ProductListStubQuery): ProductList.RawSummary.Item =
        ProductList.RawSummary.Item(
            title = query.title,
            emojiAndKeyword = emojiAndKeywordMapper.toDomain(query.emojiAndKeyword),
        )
}
