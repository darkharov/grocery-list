package app.grocery.list.domain

import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.Settings
import app.grocery.list.storage.value.kotlin.StorageValue
import app.grocery.list.storage.value.kotlin.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class AppRepository {

    protected abstract val settings: StorageValue<Settings>

    abstract val clearNotificationsReminderEnabled: StorageValue<Boolean>

    abstract fun categories(): Flow<List<Product.Category>>
    abstract fun products(): Flow<List<Product>>
    abstract fun categorizedProducts(criteria: CategorizedProductsCriteria = CategorizedProductsCriteria.All): Flow<List<CategoryAndProducts>>
    abstract fun numberOfAddedProducts(): Flow<Int>
    abstract fun numberOfEnabledProducts(): Flow<Int>
    abstract fun productListCount(): Flow<Int>
    abstract fun atLeastOneProductEnabled(): Flow<Boolean>
    abstract fun sampleProducts(): Flow<List<Product>>
    abstract suspend fun productTitleAndCategory(productId: Int): Product.TitleAndCategory

    abstract suspend fun findCategory(search: String): Product.Category?
    abstract suspend fun category(id: Int): Product.Category
    abstract suspend fun findEmoji(search: String): EmojiSearchResult?
    abstract suspend fun clearProducts()
    abstract suspend fun deleteProduct(productId: Int): Product
    abstract suspend fun putProduct(product: Product)
    abstract suspend fun putProducts(products: List<Product>)
    abstract suspend fun setProductEnabled(productId: Int, enabled: Boolean)
    abstract suspend fun enableAll()
    abstract suspend fun disableAll()

    fun productTitleFormat(): Flow<ProductTitleFormat> =
        settings
            .observe()
            .map {
                it.productTitleFormat
            }

    suspend fun setProductItemFormat(mode: ProductTitleFormat) {
        settings.edit { settings ->
            settings.copy(
                productTitleFormat = mode,
            )
        }
    }

    fun enabledAndDisabledProducts(): Flow<EnabledAndDisabledProducts> =
        products()
            .map { products ->
                EnabledAndDisabledProducts(
                    all = products,
                    enabled = products.filter { it.enabled },
                    disabled = products.filterNot { it.enabled },
                )
            }

    enum class CategorizedProductsCriteria {
        All,
        EnabledOnly,
    }
}
