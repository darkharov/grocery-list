package format

import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import app.grocery.list.domain.format.sharing.SharingStringFormatter
import app.grocery.list.domain.search.EmojiAndCategoryId
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SharingStringFormatterTest {

    private val productTitle = "Apple 5 pcs"
    private val productTitleWithPostfix = "$productTitle\n--------\n"

    private val emojiAndCategoryId =
        EmojiAndCategoryId(
            emoji = EmojiSearchResult(
                emoji = "🍎",
                keyword = "Apple",
            ),
            categoryId = 42,
        )

    private val prototype = Product(
        id = 0,
        title = productTitle,
        emojiSearchResult = emojiAndCategoryId.emoji,
        categoryId = emojiAndCategoryId.categoryId,
        enabled = true,
    )

    @Test
    fun `product with no emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype.copy(emojiSearchResult = null),
        )
    }

    @Test
    fun `product with emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype,
        )
    }

    private fun `one product formatted and parsed correctly`(
        prototype: Product,
    ) {
        val emojiAndCategoryId = EmojiAndCategoryId(
            emoji = prototype.emojiSearchResult,
            categoryId = prototype.categoryId,
        )
        val formatter = SharingStringFormatter(ContractMock(emojiAndCategoryId))
        val actualFormatWithPostfix = formatter.toSharingString(listOf(prototype), recommendUsingThisApp = true)
        assert(actualFormatWithPostfix == productTitleWithPostfix)

        val actualFormat = formatter.toSharingString(listOf(prototype), recommendUsingThisApp = false)
        assert(actualFormat == productTitle)

        val parsed = runBlocking {
            formatter.parse(actualFormat).getOrThrow().first()
        }
        assert(parsed == prototype)
    }

    private class ContractMock(
        private val emojiAndCategoryId: EmojiAndCategoryId,
    ) : SharingStringFormatter.Contract {

        override fun recommendationToUseThisApp(): String =
            ""

        override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
            emojiAndCategoryId
    }
}
