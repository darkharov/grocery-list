package app.grocery.list.domain.product

import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun get(criteria: ProductsCriteria): Flow<List<Product>>
    fun count(): Flow<Int>
    fun numberOfEnabled(): Flow<Int>
    fun atLeastOneEnabled(): Flow<Boolean>
    fun samples(): Flow<List<Product>>
    fun isThereAtLeastOne(): Flow<Boolean>

    suspend fun get(id: Int): Product
    suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    suspend fun findEmoji(search: CharSequence): EmojiAndKeyword?
    suspend fun deleteAll()
    suspend fun delete(productId: Int): Product
    suspend fun put(product: Product)
    suspend fun put(products: List<Product>)
    suspend fun setEnabled(productId: Int, enabled: Boolean)
    suspend fun setEnabled(productIds: List<Int>, enabled: Boolean)
    suspend fun enableAll()
    suspend fun disableAll()
}
