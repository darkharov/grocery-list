package app.grocery.list.commons.format

import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import org.junit.Test

internal class ProductListToStringFormatterTest {

    private val prototype = Product(
        id = 0,
        title = "Apple 5 pcs",
        emojiSearchResult = EmojiSearchResult(
            emoji = "🍎",
            keyword = "Apple",
        ),
        categoryId = 42,
    )

    @Test
    fun `product with no emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype.copy(emojiSearchResult = null),
            expectedFormat = "Apple 5 pcs|42",
        )
    }

    @Test
    fun `product with emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype,
            expectedFormat = "Apple 5 pcs|42|🍎|Apple",
        )
    }

    private fun `one product formatted and parsed correctly`(
        prototype: Product,
        expectedFormat: String,
    ) {
        val formatter = ProductListToStringFormatter()
        val actualFormat = formatter.printWithoutEncoding(listOf(prototype))
        assert(actualFormat == expectedFormat)

        val parsed = formatter.parseWithoutDecoding(actualFormat).first()
        assert(parsed == prototype)
    }
}
