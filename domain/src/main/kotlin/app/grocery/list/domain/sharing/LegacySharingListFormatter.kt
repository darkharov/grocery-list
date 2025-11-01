package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.EmojiSearchResult
import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Deprecated("This format is not longer supported")
@Singleton
internal class LegacySharingStringFormatter @Inject internal constructor() {

    @OptIn(ExperimentalEncodingApi::class)
    fun parse(string: String): Result<List<Product>> {
        return runCatching {
            val valuable = string.substringBefore(DELIMITER)
            val decoded = Base64.Default.decode(valuable)
            val stringToParse = decoded.decodeToString()
            parseWithoutDecoding(stringToParse)
        }
    }

    private fun parseWithoutDecoding(productList: String): List<Product> =
        productList
            .split(LIST_ITEM_SEPARATOR)
            .map(::parseProduct)

    private fun parseProduct(product: String): Product {
        val parts = product.split(FIELDS_SEPARATOR).iterator()
        return Product(
            id = 0,
            title = parts.next(),
            categoryId = parts.next().toInt(),
            emojiSearchResult = if (parts.hasNext()) {
                EmojiSearchResult(
                    emoji = parts.next(),
                    keyword = parts.next(),
                )
            } else {
                null
            },
            enabled = true,
        )
    }

    companion object {
        private const val FIELDS_SEPARATOR = '|'
        private const val LIST_ITEM_SEPARATOR = '\n'
        private const val DELIMITER = "\n--------\n" // DO NOT UPDATE THIS VALUE!
    }
}
