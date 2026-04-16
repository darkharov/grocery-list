package app.grocery.list.data.product.list.summary.content.stub

import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListContentStubMapper @Inject constructor(
    private val emojiAndKeywordMapper: EmojiAndKeywordMapper,
) {
    fun toDomain(item: ProductListContentStubQuery): ProductList.RawSummary.Item =
        ProductList.RawSummary.Item(
            title = item.title,
            emojiAndKeyword = emojiAndKeywordMapper.toDomain(item.emojiAndKeyword),
        )
}
