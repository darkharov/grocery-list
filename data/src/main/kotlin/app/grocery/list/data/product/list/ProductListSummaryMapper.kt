package app.grocery.list.data.product.list

import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListSummaryMapper @Inject constructor(
    private val emojiAndKeywordMapper: EmojiAndKeywordMapper,
) {
    fun toDomain(query: ProductListSummaryQuery): ProductList.RawSummary {
        val databaseId = query.productList.id
        val id = if (databaseId != null) {
            ProductList.Id.Custom(id = databaseId)
        } else {
            ProductList.Id.Default
        }
        return ProductList.RawSummary(
            productList = ProductList(
                id = id,
                title = query.productList.title,
                colorScheme = ProductList.ColorScheme
                    .entries[query.productList.colorScheme],
            ),
            totalSize = query.totalSize,
            sampleItems = query.sampleItems.map {
                ProductList.RawSummary.SampleItem(
                    title = it.title,
                    emojiAndKeyword =
                        emojiAndKeywordMapper.toDomain(
                            projection = it.emojiAndKeyword,
                        )
                )
            },
        )
    }

    fun toData(createParams: ProductList.CreateParams): CustomProductListEntity =
        CustomProductListEntity(
            id = null,
            title = createParams.title,
            colorScheme = createParams.colorScheme.ordinal,
        )
}
