package app.grocery.list.domain.product

import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun categorizedProducts(criteria: CategorizedProductsCriteria): Flow<List<CategoryProducts>>
    fun numberOfProducts(): Flow<Int>
    fun numberOfEnabledProducts(): Flow<Int>
    fun atLeastOneProductEnabled(): Flow<Boolean>
    fun sampleProducts(): Flow<List<Product>>
    fun enabledAndDisabledProducts(): Flow<EnabledAndDisabledProducts>
    fun isThereAtLeastOne(): Flow<Boolean>

    suspend fun productTitleAndCategoryId(productId: Int): ProductTitleAndCategoryId
    suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    suspend fun findEmoji(search: String): EmojiAndKeyword?
    suspend fun clearProducts()
    suspend fun deleteProduct(productId: Int): Product
    suspend fun putProduct(product: Product)
    suspend fun putProducts(products: List<Product>)
    suspend fun setProductEnabled(productId: Int, enabled: Boolean)
    suspend fun setProductsEnabled(productIds: List<Int>, enabled: Boolean)
    suspend fun enableAllProducts()
    suspend fun disableAllProducts()

    enum class CategorizedProductsCriteria {
        All,
        EnabledOnly,
    }
}
