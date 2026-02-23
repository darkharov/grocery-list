package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface CustomProductListRepository {
    fun all(): Flow<List<ProductList.Custom>>
    fun get(id: Int): Flow<ProductList.Custom?>
    fun contains(id: Int): Flow<Boolean>
    suspend fun put(params: ProductList.Custom.CreateParams)
}
