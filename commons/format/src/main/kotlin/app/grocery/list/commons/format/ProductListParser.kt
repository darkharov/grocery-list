package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductListParser @Inject constructor(
    private val sharingStringFormatter: SharingStringFormatter,
    private val handTypedListParser: HandTypedListParser,
) {
    suspend fun parse(string: String): Result<List<Product>> {
        sharingStringFormatter.parse(string).onSuccess { products ->
            return Result.success(products)
        }
        handTypedListParser.parse(string).onSuccess { products ->
            return Result.success(products)
        }
        return Result.failure(ProductsNotFoundException())
    }

    class ProductsNotFoundException : Exception()
}
