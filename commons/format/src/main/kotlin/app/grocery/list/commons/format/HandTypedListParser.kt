package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandTypedListParser @Inject constructor() {

    fun parse(string: String): Result<List<Product>> {

        val products =
            string
                .split(*Delimiters)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { title ->
                    Product(
                        id = 0,
                        title = title.replaceFirstChar { it.titlecaseChar() },
                        emojiSearchResult = null,
                        enabled = true,
                        categoryId = 0,
                    )
                }

        return if (products.isNotEmpty()) {
            Result.success(products)
        } else {
            Result.failure(ProductsNotFoundException())
        }
    }

    companion object {
        private val Delimiters = arrayOf(",", ";", "\n")
    }

    class ProductsNotFoundException : Exception()
}
