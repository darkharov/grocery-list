package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductListParser @Inject constructor(
    private val sharingStringFormatter: SharingStringFormatter,
    private val handTypedListParser: HandTypedListParser,
) {
    suspend fun parse(string: String): Result {

        sharingStringFormatter.parse(string).onSuccess { products ->
            return Result.EncodedStringParsed(products)
        }

        handTypedListParser.parse(string).onSuccess { products ->
            return Result.HandTypedStringParsed(
                string = string,
                products = products,
            )
        }

        return Result.ProductsNotFound
    }

    sealed class Result {

        data object ProductsNotFound : Result()

        data class EncodedStringParsed(
            val products: List<Product>,
        ) : Result()

        data class HandTypedStringParsed(
            val string: String,
            val products: List<Product>,
        ) : Result()
    }
}
