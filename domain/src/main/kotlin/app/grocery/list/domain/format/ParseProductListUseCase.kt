package app.grocery.list.domain.format

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.Product
import app.grocery.list.storage.value.kotlin.get
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class ParseProductListUseCase @Inject constructor(
    private val repository: AppRepository,
    private val formatter: SharingStringFormatter,
    private val legacyFormatter: LegacySharingStringFormatter,
) {
    suspend fun execute(
        text: String,
        separator: ProductListSeparator,
    ): Result<FormattedProducts> {
        legacyFormatter.parse(text).onSuccess { products ->
            return result(products, separator)
        }
        formatter.parse(text).onSuccess { products ->
            return result(products, separator)
        }
        return Result.failure(ProductsNotFoundException())
    }

    private suspend fun result(products: List<Product>, separator: ProductListSeparator) =
        Result.success(
            FormattedProducts(
                items = products,
                formattedString = repository
                    .productTitleFormatter
                    .get()
                    .printToString(products, separator),
            )
        )

    class ProductsNotFoundException : Exception()
}
