package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.ProductList
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun get(criteria: Product.Criteria): Flow<List<Product>>
    fun count(criteria: Product.Criteria = Product.Criteria(false, ProductList.Id.Default)): Flow<Int>
    fun totalNumber(): Flow<Int>
    fun samples(): Flow<List<Product>>
    fun any(criteria: Product.Criteria): Flow<Boolean>

    suspend fun get(id: Int): Product
    suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId
    suspend fun findEmoji(search: CharSequence): EmojiAndKeyword?
    suspend fun deleteAll(productListId: ProductList.Id)
    suspend fun delete(productId: Int): Product
    suspend fun put(product: Product)
    suspend fun put(products: List<Product>)
    suspend fun setEnabled(productId: Int, enabled: Boolean)
    suspend fun setEnabled(productIds: List<Int>, enabled: Boolean)
    suspend fun enableAll(productListId: ProductList.Id)
    suspend fun disableAll(productListId: ProductList.Id)
}
