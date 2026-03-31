package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow

interface ProductListRepository {

    suspend fun put(params: ProductList.PutParams)
    suspend fun delete(id: ProductList.Id.Custom)
    suspend fun setSelectedOne(newValue: ProductList.Id)
    suspend fun setCustomListsFunctionState(newValue: CustomProductListsSetting.Customizable)

    fun all(): Flow<List<ProductList>>
    fun get(id: ProductList.Id): Flow<ProductList>
    fun allSummarized(): Flow<List<ProductList.RawSummary>>
    fun selectedOne(): Flow<ProductList>
    fun idOfSelectedOne(): Flow<ProductList.Id>
    fun customListsSetting(): Flow<CustomProductListsSetting>
    fun titleOfCurrentCustomListOrNull(): Flow<String?>
}
