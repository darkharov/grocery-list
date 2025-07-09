package app.grocery.list.domain

import app.grocery.list.storage.value.kotlin.StorageValue
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    val clearNotificationsReminderEnabled: StorageValue<Boolean>

    fun categories(): Flow<List<Product.Category>>
    fun productList(): Flow<List<CategoryAndProducts>>
    fun numberOfAddedProducts(): Flow<Int>

    suspend fun findCategory(search: String): Product.Category?
    suspend fun findEmoji(search: String): String?
    suspend fun clearProducts()
    suspend fun deleteProduct(productId: Int)
    suspend fun putProduct(product: Product)
}
