package app.grocery.list.data

import android.content.Context
import app.grocery.list.commons.format.ProductListToStringFormatter
import app.grocery.list.data.db.ProductDao
import app.grocery.list.data.db.ProductEntity
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.Settings
import app.grocery.list.storage.value.android.StorageValueDelegates
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Singleton
internal class AppRepositoryImpl @Inject constructor(
    delegates: StorageValueDelegates,
    @ApplicationContext
    private val context: Context,
    private val productDao: ProductDao,
    private val productMapper: ProductEntity.Mapper,
    private val productItemFormatMapper: ProductItemFormatMapper,
    private val categoryDao: CategoryDao,
    private val productListFormatter: ProductListToStringFormatter,
) : AppRepository() {

    override val settings by delegates.custom<Settings>(
        write = { settings ->
            int(
                PRODUCT_TITLE_FORMAT_ID,
                productItemFormatMapper.toInt(settings.productTitleFormat),
            )
        },
        read = {
            val defaultValue = productItemFormatMapper.toInt(ProductTitleFormat.EmojiAndFullText)
            val id = int(PRODUCT_TITLE_FORMAT_ID, defaultValue = defaultValue)
            Settings(
                productTitleFormat = productItemFormatMapper.fromInt(id = id),
            )
        },
    )

    override val clearNotificationsReminderEnabled by delegates.boolean(defaultValue = true)

    override fun categories(): Flow<List<Product.Category>> =
        categoryDao.categories()

    override suspend fun findCategory(search: String): Product.Category? =
        categoryDao.category(search = search)

    override suspend fun findEmoji(search: String): EmojiSearchResult? =
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

    override suspend fun setProductEnabled(productId: Int, enabled: Boolean) {
        productDao.setProductEnabled(productId = productId, enabled = enabled)
    }

    override fun products(): Flow<List<Product>> =
        productDao.selectProducts()
            .map { productMapper.toDomainModels(it) }

    override fun categorizedProducts(
        criteria: CategorizedProductsCriteria,
    ): Flow<List<CategoryAndProducts>> =
        productDao
            .select(
                enabledOnly = when (criteria) {
                    CategorizedProductsCriteria.All -> false
                    CategorizedProductsCriteria.EnabledOnly -> true
                }
            )
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

    override fun productListEmpty(): Flow<Boolean> =
        productDao.productListEmpty()

    override fun sampleProducts(): Flow<List<Product>> =
        flowOf(
            productListFormatter.parseWithoutDecoding(
                productList = context.getString(R.string.sample_products),
            )
        )

    companion object {
        private const val PRODUCT_TITLE_FORMAT_ID = "PRODUCT_TITLE_FORMAT_ID"
    }
}
