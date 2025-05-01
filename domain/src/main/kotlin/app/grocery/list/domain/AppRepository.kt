package app.grocery.list.domain

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun categories(): List<Product.Category>
    suspend fun findCategory(search: String): Product.Category?
    suspend fun clearProducts()
    suspend fun deleteProduct(productId: Int)
    suspend fun putProduct(product: Product)
    fun getProductList(): Flow<List<CategoryAndProducts>>
    fun getProductListCount(): Flow<Int>
}
