package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface ProductListRepository {

    suspend fun put(params: ProductList.CreateParams)
    suspend fun setSelectedOne(newValue: ProductList.Id)
    suspend fun setFeatureEnabled(enabled: Boolean)

    fun all(): Flow<List<ProductList.RawSummary>>
    fun featureState(): Flow<CustomProductListsFeatureState>
    fun idOfSelectedOne(): Flow<ProductList.Id>
}
