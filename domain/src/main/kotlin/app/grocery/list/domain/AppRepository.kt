package app.grocery.list.domain

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
    abstract fun categorizedProducts(): Flow<List<CategoryAndProducts>>
    abstract fun numberOfAddedProducts(): Flow<Int>

    abstract suspend fun findCategory(search: String): Product.Category?
    abstract suspend fun findEmoji(search: String): EmojiSearchResult?
    abstract suspend fun clearProducts()
    abstract suspend fun deleteProduct(productId: Int)
    abstract suspend fun putProduct(product: Product)
    abstract suspend fun putProducts(products: List<Product>)

    fun itemInNotificationMode(): Flow<Settings.ItemInNotificationMode> =
        settings
            .observe()
            .map {
                it.itemInNotificationMode
            }

    suspend fun setItemInNotificationMode(mode: Settings.ItemInNotificationMode) {
        settings.edit { settings ->
            settings.copy(
                itemInNotificationMode = mode,
            )
        }
    }
}
