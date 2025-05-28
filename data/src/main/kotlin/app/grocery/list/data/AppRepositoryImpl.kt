package app.grocery.list.data

import app.grocery.list.data.db.ProductDao
import app.grocery.list.data.db.ProductEntity
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class AppRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productMapper: ProductEntity.Mapper,
    private val categoryDao: CategoryDao,
) : AppRepository {

    override suspend fun categories(): List<Product.Category> =
        categoryDao.all()

    override suspend fun findCategory(search: String): Product.Category? =
        categoryDao.find(search = search)

    override suspend fun clearProducts() {
        productDao.deleteAll()
    }

    override suspend fun deleteProduct(productId: Int) {
        productDao.delete(productId = productId)
    }

    override suspend fun putProduct(product: Product) {
        val entity = productMapper.toDataEntity(product)
        productDao.insert(entity)
    }

    override fun getProductList(): Flow<List<CategoryAndProducts>> =
        productDao
            .select()
            .map { categorizedProducts ->
                categorizedProducts.map { (categoryId, products) ->
                    CategoryAndProducts(
                        category = categoryDao.all().first { it.id == categoryId },
                        products = productMapper.toDomainModels(products),
                    )
                }
            }

    override fun getNumberOfAddedProducts(): Flow<Int> =
        productDao.count()

    override fun atLeastOneProductAdded(): Flow<Boolean> =
        productDao.count().map { it > 0 }
}
