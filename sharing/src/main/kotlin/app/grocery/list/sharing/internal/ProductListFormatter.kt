package app.grocery.list.sharing.internal

import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import org.jetbrains.annotations.VisibleForTesting

@Singleton
internal class ProductListFormatter @Inject constructor() {

    @OptIn(ExperimentalEncodingApi::class)
    fun print(productList: List<Product>, suffix: String): String {
        val formatted = printWithoutEncoding(productList)
        val bytes = formatted.encodeToByteArray()
        val encoded = Base64.Default.encode(bytes)
        return encoded + DELIMITER + suffix
    }

    @VisibleForTesting
    fun printWithoutEncoding(productList: List<Product>): String =
        productList.joinToString(
            separator = LIST_ITEM_SEPARATOR.toString(),
            transform = { it.print() },
        )

    private fun Product.print(): String =
        buildString {

            title.filterNotTo(this) {
                it == FIELDS_SEPARATOR || it == LIST_ITEM_SEPARATOR
            }

            append(FIELDS_SEPARATOR)
            append(categoryId)

            val emoji = emojiSearchResult
            if (emoji != null) {
                append(FIELDS_SEPARATOR)
                append(emoji.emoji)
                append(FIELDS_SEPARATOR)
                append(emoji.keyword)
            }
        }

    @OptIn(ExperimentalEncodingApi::class)
    fun parse(message: String): Result<List<Product>> {
        return runCatching {
            val valuable = message.substringBefore(DELIMITER)
            val decoded = Base64.Default.decode(valuable)
            val stringToParse = decoded.decodeToString()
            parseWithoutDecoding(stringToParse)
        }
    }

    @VisibleForTesting
    fun parseWithoutDecoding(productList: String): List<Product> =
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
        )
    }

    companion object {
        private const val FIELDS_SEPARATOR = '|'
        private const val LIST_ITEM_SEPARATOR = '\n'
        private const val DELIMITER = "\n--------\n" // DO NOT UPDATE THIS VALUE!
    }
}
