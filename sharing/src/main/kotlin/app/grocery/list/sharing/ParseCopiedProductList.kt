package app.grocery.list.sharing

import app.grocery.list.domain.Product
import app.grocery.list.sharing.internal.ProductListFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParseCopiedProductList @Inject internal constructor(
    private val formatter: ProductListFormatter,
) {
    fun execute(message: String): Result<List<Product>> =
        formatter.parse(message = message)
}
