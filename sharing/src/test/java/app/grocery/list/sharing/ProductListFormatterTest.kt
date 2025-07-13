package app.grocery.list.sharing

import app.grocery.list.domain.Product
import app.grocery.list.sharing.internal.ProductListFormatter
import org.junit.Test

internal class ProductListFormatterTest {

    private val prototype = Product(
        id = 0,
        title = "Apple",
        emoji = "🍎",
        categoryId = 42,
    )

    @Test
    fun `product with no emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype.copy(emoji = null),
            expectedFormat = "Apple|42",
        )
    }

    @Test
    fun `product with emoji is correct`() {
        `one product formatted and parsed correctly`(
            prototype = prototype,
            expectedFormat = "Apple|42|🍎",
        )
    }

    private fun `one product formatted and parsed correctly`(
        prototype: Product,
        expectedFormat: String,
    ) {
        val formatter = ProductListFormatter()
        val actualFormat = formatter.printWithoutEncoding(listOf(prototype))
        assert(actualFormat == expectedFormat)

        val parsed = formatter.parseWithoutDecoding(actualFormat).first()
        assert(parsed == prototype)
    }
}
