package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class ParseProductListUseCase @Inject constructor(
    private val formatter: SharingStringFormatter,
    private val legacyFormatter: LegacySharingStringFormatter,
) {
    suspend fun execute(text: String): Result<List<Product>> {
        legacyFormatter.parse(text).onSuccess { products ->
            return Result.success(products)
        }
        formatter.parse(text).onSuccess { products ->
            return Result.success(products)
        }
        return Result.failure(ProductsNotFoundException())
    }

    class ProductsNotFoundException : Exception()
}
