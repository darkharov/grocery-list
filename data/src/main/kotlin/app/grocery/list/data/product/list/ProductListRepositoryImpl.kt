package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
internal class ProductListRepositoryImpl @Inject constructor(
    private val dao: ProductListDao,
    private val mapper: ProductListSummaryMapper,
) : ProductListRepository {

    override fun all(): Flow<List<ProductList.RawSummary>> =
        dao.all()
            .map { entities ->
                entities.map(mapper::toDomain)
            }
            .flowOn(Dispatchers.IO)

    override suspend fun put(params: ProductList.CreateParams) {
        withContext(Dispatchers.IO) {
            dao.insert(mapper.toData(params))
        }
    }
}
