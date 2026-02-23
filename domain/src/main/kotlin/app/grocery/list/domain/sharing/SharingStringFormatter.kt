package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SharingStringFormatter @Inject constructor(
    @param:Named(RECOMMENDATION_TO_USE_APP)
    private val recommendationToUseApp: String,
    private val repository: ProductRepository,
) {
    suspend fun parse(sharingString: String): Result<List<Product>> {

        val products =
            sharingString
                .substringBefore(POSTFIX_SEPARATOR)
                .split(*Delimiters)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { title ->
                    val emojiAndCategoryId = repository.findEmojiAndCategoryId(search = title)
                    val product = Product(
                        id = 0,
                        title = title.replaceFirstChar { it.titlecaseChar() },
                        emojiAndKeyword = emojiAndCategoryId.emoji,
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

    fun toSharingString(
        products: List<Product>,
        includeRecommendationToUseApp: Boolean,
    ): String {
        val postfix = if (includeRecommendationToUseApp) {
            POSTFIX_SEPARATOR + recommendationToUseApp
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

        const val RECOMMENDATION_TO_USE_APP = "app.grocery.list.domain.sharing.SharingStringFormatter.RECOMMENDATION_TO_USE_APP"
    }

    class ProductsNotFoundException : Exception()
}
