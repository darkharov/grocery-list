package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.CustomProductListRepository
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
internal class CustomProductListRepositoryImpl @Inject constructor(
    private val dao: CustomProductListDao,
    private val mapper: CustomProductListMapper,
) : CustomProductListRepository {

    override fun all(): Flow<List<ProductList.Custom>> =
        dao.all()
            .map(mapper::listToDomain)
            .flowOn(Dispatchers.IO)

    override fun get(id: Int): Flow<ProductList.Custom?> =
        dao.select(id = id)
            .map { mapper.nullableToDomain(it) }
            .flowOn(Dispatchers.IO)

    override fun contains(id: Int): Flow<Boolean> =
        dao.contains(id = id)

    override suspend fun put(params: ProductList.Custom.CreateParams) {
        withContext(Dispatchers.IO) {
            dao.insert(mapper.toData(params))
        }
    }
}
