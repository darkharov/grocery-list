package app.grocery.list.domain

import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun categories(): Flow<List<Product.Category>>
    fun productList(): Flow<List<CategoryAndProducts>>
    fun numberOfAddedProducts(): Flow<Int>

    suspend fun findCategory(search: String): Product.Category?
    suspend fun clearProducts()
    suspend fun deleteProduct(productId: Int)
    suspend fun putProduct(product: Product)
}
