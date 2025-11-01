package app.grocery.list.domain.format.notification

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.format.printToString
import app.grocery.list.domain.product.CategoryAndProducts
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.storage.value.kotlin.get
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class FormatProductsForNotificationsUseCase @Inject internal constructor(
    private val productRepository: ProductRepository,
    private val appRepository: AppRepository,
) {
    suspend fun execute(maxNumberOfItems: Int): List<NotificationContent> {
        val formatter = productTitleFormatter()
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

    private suspend fun productTitleFormatter(): ProductTitleFormatter =
        appRepository.productTitleFormatter.get()

    private suspend fun enabledProducts() =
        productRepository
            .categorizedProducts(
                criteria = ProductRepository.CategorizedProductsCriteria.EnabledOnly,
            )
            .first()

    private fun groupProducts(
        products: List<CategoryAndProducts>,
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
            formattedProductTitles = formatter.printToString(
                products = sortedGroup,
                separator = ProductListSeparator.Notifications,
            ),
        )
    }
}
