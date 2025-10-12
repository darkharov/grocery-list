package app.grocery.list.domain

import app.grocery.list.domain.search.EmojiAndCategoryId
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.storage.value.kotlin.StorageValue
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    val productTitleFormat: StorageValue<ProductTitleFormat>
    val clearNotificationsReminderEnabled: StorageValue<Boolean>
    val bottomBarRoadmapStep: StorageValue<BottomBarRoadmapStep>

    fun categories(): Flow<List<Product.Category>>
    fun products(): Flow<List<Product>>
    fun categorizedProducts(criteria: CategorizedProductsCriteria): Flow<List<CategoryAndProducts>>
    fun numberOfProducts(): Flow<Int>
    fun numberOfEnabledProducts(): Flow<Int>
    fun atLeastOneProductEnabled(): Flow<Boolean>
    fun sampleProducts(): Flow<List<Product>>
    fun enabledAndDisabledProducts(): Flow<EnabledAndDisabledProducts>

    suspend fun productTitleAndCategory(productId: Int): Product.TitleAndCategory
    suspend fun findCategory(search: String): Product.Category?
    suspend fun category(id: Int): Product.Category
    suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    suspend fun findEmoji(search: String): EmojiSearchResult?
    suspend fun clearProducts()
    suspend fun deleteProduct(productId: Int): Product
    suspend fun putProduct(product: Product)
    suspend fun putProducts(products: List<Product>)
    suspend fun setProductEnabled(productId: Int, enabled: Boolean)
    suspend fun enableAllProducts()
    suspend fun disableAllProducts()

    enum class CategorizedProductsCriteria {
        All,
        EnabledOnly,
    }
}
