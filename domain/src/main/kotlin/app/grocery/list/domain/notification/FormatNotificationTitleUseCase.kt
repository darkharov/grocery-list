package app.grocery.list.domain.notification

import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FormatNotificationTitleUseCase @Inject constructor() {

    fun execute(
        formatter: ProductTitleFormatter,
        products: List<Product>,
    ): String {
        val sorted = products
            .sortedBy { it.title.length }
            .sortedBy { it.emojiSearchResult != null }
        return formatter
            .withCommas()
            .print(sorted)
    }
}
