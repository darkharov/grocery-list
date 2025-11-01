package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.Product
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

        // Do NOT delete instances from this array
        // to maintain compatibility with the previously shared lists
        private val Delimiters = arrayOf(",", ";", "\n")

        // Do NOT update this value
        // to maintain compatibility with the previously shared lists
        private const val POSTFIX_SEPARATOR = "\n--------\n"
    }

    class ProductsNotFoundException : Exception()

    interface Contract {
        fun recommendationToUseThisApp(): String
        suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    }
}
