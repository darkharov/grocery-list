package app.grocery.list.domain.sharing

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Suppress("DEPRECATION")
@Singleton
class ParseAndFormatProductsUseCase @Inject internal constructor(
    private val formatter: SharingStringFormatter,
    private val legacyFormatter: LegacySharingStringFormatter,
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
) {
    suspend fun execute(text: String): Result<ParsedProducts> {
        legacyFormatter.parse(text).onSuccess { products ->
            return result(products)
        }
        formatter.parse(text).onSuccess { products ->
            return result(products)
        }
        return Result.failure(ProductsNotFoundException())
    }

    private suspend fun result(products: List<Product>): Result<ParsedProducts> =
        Result.success(
            ParsedProducts(
                originalProducts = products,
                formattedString = getProductTitleFormatter
                    .execute()
                    .first()
                    .formatter
                    .withNewLines()
                    .print(products),
            )
        )

    class ProductsNotFoundException : Exception()
}
