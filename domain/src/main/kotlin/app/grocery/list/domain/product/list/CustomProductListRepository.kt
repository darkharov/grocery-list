package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface CustomProductListRepository {
    fun all(): Flow<List<ProductList.Custom>>
    suspend fun put(params: ProductList.Custom.CreateParams)
}
