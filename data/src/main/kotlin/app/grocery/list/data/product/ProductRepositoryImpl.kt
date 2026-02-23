package app.grocery.list.data.product

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.data.category.CategoryDao
import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.product.ProductsCriteria
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
internal class ProductRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val productDao: ProductDao,
    private val productMapper: ProductEntity.Mapper,
    private val categoryDao: CategoryDao,
) : ProductRepository {

    override fun get(criteria: ProductsCriteria): Flow<List<Product>> =
        productDao
            .select(
                enabledOnly = criteria.enabledOnly,
                customListId = criteria.idOfSelectedCustomList,
            )
            .map(productMapper::toDomainModels)
            .flowOn(Dispatchers.IO)

    override suspend fun get(id: Int): Product {
        val entity = withContext(Dispatchers.IO) {
            productDao.select(productId = id)
        }
        return productMapper.toDomainModel(entity)
    }

    override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
        withContext(Dispatchers.IO) {
            categoryDao.emojiAndCategoryId(search = search)
        }

    override suspend fun findEmoji(search: CharSequence): EmojiAndKeyword? =
        withContext(Dispatchers.IO) {
            categoryDao.emoji(search = search)
        }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            productDao.deleteAll()
        }
    }

    override suspend fun delete(productId: Int) =
        withContext(Dispatchers.IO) {
            productMapper.toDomainModel(
                productDao.selectAndDelete(productId = productId),
            )
        }

    override suspend fun put(product: Product) {
        withContext(Dispatchers.IO) {
            val entity = productMapper.toDataEntity(product)
            productDao.insertOrReplace(entity)
        }
    }

    override suspend fun put(products: List<Product>) {
        withContext(Dispatchers.IO) {
            productDao.insertOrReplace(productMapper.toDataEntities(products))
        }
    }

    override suspend fun setEnabled(productId: Int, enabled: Boolean) {
        withContext(Dispatchers.IO) {
            productDao.setProductsEnabled(productIds = listOf(productId), enabled = enabled)
        }
    }

    override suspend fun setEnabled(productIds: List<Int>, enabled: Boolean) {
        withContext(Dispatchers.IO) {
            productDao.setProductsEnabled(productIds = productIds, enabled = enabled)
        }
    }

    override fun count(): Flow<Int> =
        productDao.count().flowOn(Dispatchers.IO)

    override fun numberOfEnabled(): Flow<Int> =
        productDao.countOfEnabled().flowOn(Dispatchers.IO)


    override fun atLeastOneEnabled(): Flow<Boolean> =
        productDao.atLeastOneProductEnabled().flowOn(Dispatchers.IO)

    override fun samples(): Flow<List<Product>> =
        flowOf(
            context.getString(R.string.sample_products)
                .split("\n")
                .map { productAsString ->
                    val parts = productAsString.split("|").iterator()
                    Product(
                        id = 0,
                        title = parts.next(),
                        categoryId = parts.next().toInt(),
                        emojiAndKeyword = EmojiAndKeyword(
                            emoji = parts.next(),
                            keyword = parts.next(),
                        ),
                        enabled = true,
                        customListId = null,
                    )
                }
        )

    override suspend fun enableAll() {
        withContext(Dispatchers.IO) {
            productDao.setEnabledFlagForAll(enabled = true)
        }
    }

    override suspend fun disableAll() {
        withContext(Dispatchers.IO) {
            productDao.setEnabledFlagForAll(enabled = false)
        }
    }

    override fun isThereAtLeastOne(): Flow<Boolean> =
        productDao.isThereAtLeastOne()
            .flowOn(Dispatchers.IO)
}
