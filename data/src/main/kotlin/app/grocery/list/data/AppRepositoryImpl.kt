package app.grocery.list.data

import android.content.Context
import app.grocery.list.data.db.ProductDao
import app.grocery.list.data.db.ProductEntity
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.AppRepository.CategorizedProductsCriteria
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.EnabledAndDisabledProducts
import app.grocery.list.domain.Product
import app.grocery.list.domain.search.EmojiAndCategoryId
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
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
    private val categoryDao: CategoryDao,
) : AppRepository {

    override val productTitleFormat by delegates.enum(defaultValue = ProductTitleFormat.EmojiAndFullText)
    override val clearNotificationsReminderEnabled by delegates.boolean(defaultValue = true)
    override val bottomBarRoadmapStep by delegates.enum(defaultValue = BottomBarRoadmapStep.Initial)

    override fun categories(): Flow<List<Product.Category>> =
        categoryDao.categories()

    override suspend fun productTitleAndCategory(productId: Int): Product.TitleAndCategory {
        val (title, categoryId) = productDao.selectTitleAndCategoryId(productId = productId)
        return Product.TitleAndCategory(
            productTitle = title,
            category = categoryDao.get(
                id = categoryId,
            ),
        )
    }

    override suspend fun findCategory(search: String): Product.Category? =
        categoryDao.category(search = search)

    override suspend fun findEmojiAndCategoryId(search: String): EmojiAndCategoryId =
        categoryDao.emojiAndCategoryId(search = search)

    override suspend fun category(id: Int): Product.Category =
        categoryDao.get(id = id)

    override suspend fun findEmoji(search: String): EmojiSearchResult? =
        categoryDao.emoji(search = search)

    override suspend fun clearProducts() {
        productDao.deleteAll()
    }

    override suspend fun deleteProduct(productId: Int) =
        productMapper.toDomainModel(
            productDao.selectAndDelete(productId = productId),
        )

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

    override fun numberOfProducts(): Flow<Int> =
        productDao.count()

    override fun numberOfEnabledProducts(): Flow<Int> =
        productDao.countOfEnabled()

    override fun atLeastOneProductEnabled(): Flow<Boolean> =
        productDao.atLeastOneProductEnabled()

    override fun sampleProducts(): Flow<List<Product>> =
        flowOf(
            context.getString(R.string.sample_products)
                .split("\n")
                .map { productAsString ->
                    val parts = productAsString.split("|").iterator()
                    Product(
                        id = 0,
                        title = parts.next(),
                        categoryId = parts.next().toInt(),
                        emojiSearchResult = EmojiSearchResult(
                            emoji = parts.next(),
                            keyword = parts.next(),
                        ),
                        enabled = true,
                    )
                }
        )

    override suspend fun enableAllProducts() {
        productDao.setEnabledFlagForAll(enabled = true)
    }

    override suspend fun disableAllProducts() {
        productDao.setEnabledFlagForAll(enabled = false)
    }

    override fun enabledAndDisabledProducts(): Flow<EnabledAndDisabledProducts> =
        productDao
            .select(enabledOnly = false)
            .map { productMapper.toDomainModels(it.values.flatten()) }
            .map { products ->
                EnabledAndDisabledProducts(
                    all = products,
                    enabled = products.filter { it.enabled },
                    disabled = products.filterNot { it.enabled },
                )
            }
}
