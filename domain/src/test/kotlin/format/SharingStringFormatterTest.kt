package format

import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.sharing.SharingStringFormatter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SharingStringFormatterTest {

    private val productTitle = "Apple 5 pcs"
    private val productTitleWithPostfix = "$productTitle\n--------\n"

    private val emojiAndCategoryId =
        EmojiAndCategoryId(
            emoji = EmojiAndKeyword(
                emoji = "🍎",
                keyword = "Apple",
            ),
            categoryId = 42,
        )

    private val prototype = Product(
        id = 0,
        title = productTitle,
        emojiAndKeyword = emojiAndCategoryId.emoji,
        categoryId = emojiAndCategoryId.categoryId,
        enabled = true,
    )

    @Test
    fun `product with no emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype.copy(emojiAndKeyword = null),
        )
    }

    @Test
    fun `product with emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype,
        )
    }

    private fun `one product formatted and parsed correctly`(prototype: Product) {

        val repository = mockk<ProductRepository>()
        val emojiAndCategoryId = EmojiAndCategoryId(
            emoji = prototype.emojiAndKeyword,
            categoryId = prototype.categoryId,
        )
        coEvery { repository.findEmojiAndCategoryId(any()) } returns emojiAndCategoryId
        val formatter = SharingStringFormatter(
            recommendationToUseApp = "",
            repository = repository,
        )
        val actualFormatWithPostfix = formatter.toSharingString(listOf(prototype), includeRecommendationToUseApp = true)
        assert(actualFormatWithPostfix == productTitleWithPostfix)

        val actualFormat = formatter.toSharingString(listOf(prototype), includeRecommendationToUseApp = false)
        assert(actualFormat == productTitle)

        val parsed = runBlocking {
            formatter.parse(actualFormat).getOrThrow().first()
        }
        assert(parsed == prototype)
    }
}
