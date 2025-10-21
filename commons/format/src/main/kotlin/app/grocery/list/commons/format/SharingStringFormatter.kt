package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import app.grocery.list.domain.search.EmojiAndCategoryId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharingStringFormatter @Inject constructor(
    private val contract: Contract,
) {
    suspend fun parse(sharingString: String): Result<List<Product>> {

        val products =
            sharingString
                .substringBefore(POSTFIX_SEPARATOR)
                .split(*Delimiters)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { title ->
                    val emojiAndCategoryId = contract.findEmojiAndCategoryId(search = title)
                    val product = Product(
                        id = 0,
                        title = title.replaceFirstChar { it.titlecaseChar() },
                        emojiSearchResult = emojiAndCategoryId.emoji,
                        enabled = true,
                        categoryId = emojiAndCategoryId.categoryId,
                    )
                    product
                }

        return if (products.isNotEmpty()) {
            Result.success(products)
        } else {
            Result.failure(ProductsNotFoundException())
        }
    }

    fun toSharingString(products: List<Product>, recommendUsingThisApp: Boolean): String {
        val postfix = if (recommendUsingThisApp) {
            POSTFIX_SEPARATOR + contract.recommendationToUseThisApp()
        } else {
            ""
        }
        return products.joinToString(
            separator = "\n",
            postfix = postfix,
            transform = { it.title },
        )
    }

    companion object {
        private val Delimiters = arrayOf(",", ";", "\n")
        private const val POSTFIX_SEPARATOR = "\n--------\n" // DO NOT UPDATE THIS VALUE!
    }

    class ProductsNotFoundException : Exception()

    interface Contract {
        fun recommendationToUseThisApp(): String
        suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    }
}
