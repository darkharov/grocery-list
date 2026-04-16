package app.grocery.list.data.product.list

import app.grocery.list.data.internal.di.DefaultProductList
import app.grocery.list.data.product.list.custom.CustomProductListMapper
import app.grocery.list.data.product.list.setting.CustomProductListsSettingMapper
import app.grocery.list.data.product.list.summary.ProductListRawSummaryMapper
import app.grocery.list.domain.product.list.CustomProductListsSetting
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
internal class ProductListRepositoryImpl @Inject constructor(
    @param:DefaultProductList
    private val defaultProductList: ProductList,
    private val productListDao: ProductListDao,
    private val rawSummaryMapper: ProductListRawSummaryMapper,
    private val customProductListMapper: CustomProductListMapper,
    private val customProductListsSettingMapper: CustomProductListsSettingMapper,
    private val neighboursMapper: ProductListNeighboursMapper,
    storageValueFactory: StorageValueFactory,
) : ProductListRepository {

    private val customProductListsSetting = storageValueFactory.int(CUSTOM_PRODUCT_LISTS_SETTING)
    private val selectedCustomListId = storageValueFactory.nullableInt(SELECTED_CUSTOM_LIST_ID)

    override suspend fun put(params: ProductList.PutParams) {
        withContext(Dispatchers.IO) {
            val entity = customProductListMapper.toData(params)
            if (entity.id == 0) {
                productListDao.insert(entity)
            } else {
                productListDao.update(entity)
            }
        }
    }

    override suspend fun delete(id: ProductList.Id.Custom) {
        withContext(Dispatchers.IO) {
            val backingId = id.backingId
            productListDao.delete(backingId)
            selectedCustomListId.edit {
                if (it != backingId) {
                    it
                } else {
                    null
                }
            }
        }
    }

    override suspend fun setCustomListsFunctionState(newValue: CustomProductListsSetting.Customizable) {
        val mapped = customProductListsSettingMapper.toData(newValue)
        customProductListsSetting.set(mapped)
    }

    override fun all(): Flow<List<ProductList>> =
        productListDao
            .selectAll()
            .map { entities ->
                buildList {
                    add(defaultProductList)
                    addAll(
                        entities.map { entity ->
                            customProductListMapper.toDomain(entity)
                        }
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    override fun get(id: ProductList.Id): Flow<ProductList> =
        when (id) {
            is ProductList.Id.Default -> {
                flowOf(defaultProductList)
            }
            is ProductList.Id.Custom -> {
                productListDao
                    .select(id = id.backingId)
                    .map { customProductListMapper.toDomain(it!!) }
                    .flowOn(Dispatchers.IO)
            }
        }

    override fun allSummarized(): Flow<List<ProductList.RawSummary>> =
        combine(
            productListDao.stubs(),
            productListDao.selectAllWithCounters(),
            ProductListRawSummaryMapper::Params,
        ).map(rawSummaryMapper::toDomain)
            .flowOn(Dispatchers.IO)

    override fun customListsSetting(): Flow<CustomProductListsSetting> =
        customProductListsSetting
            .observe()
            .map(customProductListsSettingMapper::toDomain)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun selectedOne(): Flow<ProductList> =
        idOfSelectedOne()
            .flatMapLatest { id ->
                when (id) {
                    is ProductList.Id.Default -> {
                        flowOf(defaultProductList)
                    }
                    is ProductList.Id.Custom -> {
                        productListDao
                            .select(id = id.backingId)
                            .map { entity ->
                                if (entity == null) {
                                    defaultProductList
                                } else {
                                    customProductListMapper.toDomain(entity)
                                }
                            }
                    }
                }
            }

    override suspend fun setSelectedOne(newValue: ProductList.Id) {
        when (newValue) {
            is ProductList.Id.Custom -> {
                selectedCustomListId.set(newValue.backingId)
            }
            is ProductList.Id.Default -> {
                selectedCustomListId.set(null)
            }
        }
    }

    override fun idOfSelectedOne(): Flow<ProductList.Id> =
        combine(
            customListsSetting(),
            selectedCustomListId.observe(),
        ) { featureState, selectedCustomListId ->
            if (
                selectedCustomListId != null &&
                featureState == CustomProductListsSetting.Enabled
            ) {
                ProductList.Id.Custom(backingId = selectedCustomListId)
            } else {
                ProductList.Id.Default
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun titleOfCurrentCustomListOrNull(): Flow<String?> =
        selectedCustomListId
            .observe()
            .flatMapLatest { selectedCustomListId ->
                if (selectedCustomListId != null) {
                    productListDao
                        .selectTitleOfCustomList(id = selectedCustomListId)
                        .flowOn(Dispatchers.IO)
                } else {
                    flowOf(null)
                }
            }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun neighbours(): Flow<ProductList.Neighbours> =
        combine(
            customListsSetting(),
            selectedCustomListId.observe(),
            ::Pair,
        ).flatMapLatest { (featureState, selectedCustomListId) ->
            if (featureState != CustomProductListsSetting.Enabled) {
                flowOf(
                    ProductList.Neighbours(
                        leading = null,
                        trailing = null,
                    )
                )
            } else {
                combine(
                    productListDao.selectTrailingList(id = selectedCustomListId),
                    productListDao.selectLeadingList(id = selectedCustomListId),
                    transform = neighboursMapper::toDomain,
                )
            }
        }

    override fun containsAtLeastOneCustomList(): Flow<Boolean> =
        productListDao.containsAtLeastOneCustomList()

    companion object {
        const val OLD_KEY_CUSTOM_PRODUCT_LISTS_SETTING = "app.grocery.list.data.product.list.settings.CUSTOM_LISTS_FEATURE_STATE"
        const val CUSTOM_PRODUCT_LISTS_SETTING = "app.grocery.list.data.product.list.settings.CUSTOM_PRODUCT_LISTS_SETTING"
        const val SELECTED_CUSTOM_LIST_ID = "app.grocery.list.data.product.list.settings.SELECTED_CUSTOM_LIST_ID"
    }
}
