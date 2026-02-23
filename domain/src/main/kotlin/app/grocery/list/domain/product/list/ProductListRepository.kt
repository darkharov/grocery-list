package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface ProductListRepository {
    fun all(): Flow<List<ProductList.RawSummary>>
    suspend fun put(params: ProductList.CreateParams)
}
