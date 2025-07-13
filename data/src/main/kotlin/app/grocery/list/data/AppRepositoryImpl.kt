package app.grocery.list.data

import app.grocery.list.data.db.ProductDao
import app.grocery.list.data.db.ProductEntity
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.Product
import app.grocery.list.storage.value.android.StorageValueDelegates
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
internal class AppRepositoryImpl @Inject constructor(
    delegates: StorageValueDelegates,
    private val productDao: ProductDao,
    private val productMapper: ProductEntity.Mapper,
    private val categoryDao: CategoryDao,
) : AppRepository {

    override val clearNotificationsReminderEnabled by delegates.boolean(defaultValue = true)

    override fun categories(): Flow<List<Product.Category>> =
        categoryDao.categories()

    override suspend fun findCategory(search: String): Product.Category? =
        categoryDao.category(search = search)

    override suspend fun findEmoji(search: String): String? =
        categoryDao.emoji(search = search)

    override suspend fun clearProducts() {
        productDao.deleteAll()
    }

    override suspend fun deleteProduct(productId: Int) {
        productDao.delete(productId = productId)
    }

    override suspend fun putProduct(product: Product) {
        val entity = productMapper.toDataEntity(product)
        productDao.insertOrReplace(entity)
    }

    override suspend fun putProducts(products: List<Product>) {
        productDao.insertOrReplace(productMapper.toDataEntities(products))
    }

    override fun products(): Flow<List<Product>> =
        productDao.selectProducts()
            .map { productMapper.toDomainModels(it) }

    override fun categorizedProducts(): Flow<List<CategoryAndProducts>> =
        productDao
            .select()
            .map { categorizedProducts ->
                categorizedProducts.map { (categoryId, products) ->
                    CategoryAndProducts(
                        category = categories().first().first { it.id == categoryId },
                        products = productMapper.toDomainModels(products),
                    )
                }
            }

    override fun numberOfAddedProducts(): Flow<Int> =
        productDao.count()
}
