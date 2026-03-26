package app.grocery.list.data.product

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.data.category.CategoryDao
import app.grocery.list.data.product.list.CustomProductListIdMapper
import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.product.list.ProductList
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
    private val productMapper: ProductMapper,
    private val categoryDao: CategoryDao,
    private val customProductListIdMapper: CustomProductListIdMapper,
) : ProductRepository {

    override fun get(criteria: Product.Criteria): Flow<List<Product>> =
        productDao
            .select(
                customListId = customProductListIdMapper.toData(criteria.productListId),
                enabledOnly = criteria.enabledOnly,
            )
            .map { entities ->
                entities.map(productMapper::toDomain)
            }
            .flowOn(Dispatchers.IO)

    override suspend fun get(id: Int): Product {
        val entity = withContext(Dispatchers.IO) {
            productDao.select(productId = id)
        }
        return productMapper.toDomain(entity)
    }

    override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
        withContext(Dispatchers.IO) {
            categoryDao.emojiAndCategoryId(search = search)
        }

    override suspend fun findEmoji(search: CharSequence): EmojiAndKeyword? =
        withContext(Dispatchers.IO) {
            categoryDao.emoji(search = search)
        }

    override suspend fun deleteAll(productListId: ProductList.Id) {
        withContext(Dispatchers.IO) {
            productDao.deleteAll(
                customListId = customProductListIdMapper.toData(productListId),
            )
        }
    }

    override suspend fun delete(productId: Int) =
        withContext(Dispatchers.IO) {
            productMapper.toDomain(
                productDao.selectAndDelete(productId = productId),
            )
        }

    override suspend fun put(product: Product) {
        withContext(Dispatchers.IO) {
            val entity = productMapper.toData(product)
            productDao.insertOrReplace(entity)
        }
    }

    override suspend fun put(products: List<Product>) {
        withContext(Dispatchers.IO) {
            productDao.insertOrReplace(
                products.map(productMapper::toData)
            )
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

    override fun count(criteria: Product.Criteria): Flow<Int> =
        productDao
            .count(
                customListId = customProductListIdMapper.toData(criteria.productListId),
                enabledOnly = criteria.enabledOnly,
            )
            .flowOn(Dispatchers.IO)

    override fun numberOfEnabled(): Flow<Int> =
        productDao.countOfEnabled().flowOn(Dispatchers.IO)

    override fun any(criteria: Product.Criteria): Flow<Boolean> =
        productDao
            .any(
                customListId = customProductListIdMapper.toData(criteria.productListId),
                enabledOnly = criteria.enabledOnly,
            )
            .flowOn(Dispatchers.IO)

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
                    )
                }
        )

    override suspend fun enableAll(productListId: ProductList.Id) {
        withContext(Dispatchers.IO) {
            productDao.setEnabledFlag(
                enabled = true,
                customListId = customProductListIdMapper.toData(productListId),
            )
        }
    }

    override suspend fun disableAll(productListId: ProductList.Id) {
        withContext(Dispatchers.IO) {
            productDao.setEnabledFlag(
                enabled = false,
                customListId = customProductListIdMapper.toData(productListId),
            )
        }
    }
}
