package app.grocery.list.domain.notification

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.CategoryProducts
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@Singleton
internal class CollectNotificationsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
    private val formatNotificationTitle: FormatNotificationTitleUseCase,
) {
    fun execute(maxNumberOfItems: Int): Flow<List<NotificationContent>> =
        combine(
            getProductTitleFormatter.execute().map { it.formatter },
            productRepository.enabledOnly(),
        ) { formatter, products ->
            groupProducts(
                products = products,
                maxNumberOfNotifications = maxNumberOfItems,
            ).map { group ->
                notificationContent(group, formatter)
            }
        }

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
        return NotificationContent(
            groupKey = group.first().id,
            productIds = group.map { it.id },
            formattedProductTitles = formatNotificationTitle
                .execute(
                    formatter = formatter,
                    products = group,
                ),
        )
    }
}
