package app.grocery.list.domain.notification

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.CategoryProducts
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class GetNotificationsUseCase @Inject internal constructor(
    private val productRepository: ProductRepository,
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
    private val formatNotificationTitle: FormatNotificationTitleUseCase,
) {
    suspend fun execute(maxNumberOfItems: Int): List<NotificationContent> {
        val formatter = getProductTitleFormatter.execute().first().formatter
        val products = enabledProducts()
        val groupedProducts = groupProducts(
            products = products,
            maxNumberOfNotifications = maxNumberOfItems,
        )
        return groupedProducts
            .map { group ->
                notificationContent(group, formatter)
            }
    }

    private suspend fun enabledProducts() =
        productRepository
            .categorizedProducts(
                criteria = ProductRepository.CategorizedProductsCriteria.EnabledOnly,
            )
            .first()

    private fun groupProducts(
        products: List<CategoryProducts>,
        maxNumberOfNotifications: Int,
    ): List<List<Product>> {
        val allProducts = products.flatMap { it.products }
        val maxItemsPerNotification = 1 + (allProducts.size - 1) / maxNumberOfNotifications
        val chunked = allProducts.chunked(maxItemsPerNotification)
        return chunked
    }

    private fun notificationContent(
        group: List<Product>,
        formatter: ProductTitleFormatter,
    ): NotificationContent {
        val groupKey = group.first().id
        val productIds = group.map { it.id }
        val sortedGroup = group
            .sortedBy { it.title.length }
            .sortedBy { it.emojiSearchResult != null }
        return NotificationContent(
            groupKey = groupKey,
            productIds = productIds,
            formattedProductTitles = formatNotificationTitle
                .execute(
                    formatter = formatter,
                    products = sortedGroup,
                ),
        )
    }
}
