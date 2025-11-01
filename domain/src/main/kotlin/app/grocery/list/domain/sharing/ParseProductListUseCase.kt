package app.grocery.list.domain.sharing

import app.grocery.list.domain.SettingsRepository
import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.format.printToString
import app.grocery.list.domain.product.Product
import app.grocery.list.storage.value.kotlin.get
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class ParseProductListUseCase @Inject internal constructor(
    private val settingsRepository: SettingsRepository,
    private val formatter: SharingStringFormatter,
    private val legacyFormatter: LegacySharingStringFormatter,
) {
    suspend fun execute(
        text: String,
        separator: ProductListSeparator,
    ): Result<ParsedProducts> {
        legacyFormatter.parse(text).onSuccess { products ->
            return result(products, separator)
        }
        formatter.parse(text).onSuccess { products ->
            return result(products, separator)
        }
        return Result.failure(ProductsNotFoundException())
    }

    private suspend fun result(products: List<Product>, separator: ProductListSeparator): Result<ParsedProducts> =
        Result.success(
            ParsedProducts(
                originalProducts = products,
                formattedString = settingsRepository
                    .productTitleFormatter
                    .get()
                    .printToString(products, separator),
            )
        )

    class ProductsNotFoundException : Exception()
}
