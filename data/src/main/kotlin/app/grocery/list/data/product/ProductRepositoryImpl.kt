package app.grocery.list.data.product

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.data.category.CategoryDao
import app.grocery.list.domain.product.CategoryProducts
import app.grocery.list.domain.product.EmojiAndCategoryId
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.EnabledAndDisabledProducts
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.product.ProductTitleAndCategoryId
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Singleton
internal class ProductRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val productDao: ProductDao,
    private val productMapper: ProductEntity.Mapper,
    private val categoryDao: CategoryDao,
) : ProductRepository {

    override fun all(): Flow<List<CategoryProducts>> =
        select(enabledOnly = false)

    private fun select(enabledOnly: Boolean): Flow<List<CategoryProducts>> {
        return productDao
            .select(
                enabledOnly = enabledOnly,
            )
            .map { categorizedProducts ->
                categorizedProducts.map { (categoryId, products) ->
                    CategoryProducts(
                        categoryId = categoryId,
                        products = productMapper.toDomainModels(products),
                    )
                }
            }
    }

    override fun enabledOnly(): Flow<List<CategoryProducts>> =
        select(enabledOnly = true)

    override fun groupEnabledAndDisabled(): Flow<EnabledAndDisabledProducts> =
        productDao
            .select(enabledOnly = false)
            .map { productMapper.toDomainModels(it.values.flatten()) }
            .map { products ->
                EnabledAndDisabledProducts(
                    all = products,
                    enabled = products.filter { it.enabled },
                    disabled = products.filter { !(it.enabled) },
                )
            }

    override suspend fun productTitleAndCategoryId(productId: Int): ProductTitleAndCategoryId {
        val (title, categoryId) = productDao.selectTitleAndCategoryId(productId = productId)
        return ProductTitleAndCategoryId(
            productTitle = title,
            categoryId = categoryId,
        )
    }

    override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
        categoryDao.emojiAndCategoryId(search = search)

    override suspend fun findEmoji(search: String): EmojiAndKeyword? =
        categoryDao.emoji(search = search)

    override suspend fun deleteAll() {
        productDao.deleteAll()
    }

    override suspend fun delete(productId: Int) =
        productMapper.toDomainModel(
            productDao.selectAndDelete(productId = productId),
        )

    override suspend fun put(product: Product) {
        val entity = productMapper.toDataEntity(product)
        productDao.insertOrReplace(entity)
    }

    override suspend fun put(products: List<Product>) {
        productDao.insertOrReplace(productMapper.toDataEntities(products))
    }

    override suspend fun setEnabled(productId: Int, enabled: Boolean) {
        productDao.setProductsEnabled(productIds = listOf(productId), enabled = enabled)
    }

    override suspend fun setEnabled(productIds: List<Int>, enabled: Boolean) {
        productDao.setProductsEnabled(productIds = productIds, enabled = enabled)
    }

    override fun count(): Flow<Int> =
        productDao.count()

    override fun numberOfEnabled(): Flow<Int> =
        productDao.countOfEnabled()

    override fun atLeastOneEnabled(): Flow<Boolean> =
        productDao.atLeastOneProductEnabled()

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

    override suspend fun enableAll() {
        productDao.setEnabledFlagForAll(enabled = true)
    }

    override suspend fun disableAll() {
        productDao.setEnabledFlagForAll(enabled = false)
    }

    override fun isThereAtLeastOne(): Flow<Boolean> =
        productDao.isThereAtLeastOne()
}
