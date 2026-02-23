package app.grocery.list.data.product.list

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.domain.product.list.CustomProductListsFeatureSetting
import app.grocery.list.domain.product.list.CustomProductListsFeatureSetting.NotSet
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.theming.ColorScheme
import app.grocery.list.storage.value.android.StorageValueFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
internal class ProductListRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val productListDao: ProductListDao,
    private val productListStubMapper: ProductListStubMapper,
    private val productListCountersMapper: ProductListCountersMapper,
    private val customProductListMapper: CustomProductListMapper,
    storageValueFactory: StorageValueFactory,
) : ProductListRepository {

    private val featureState = storageValueFactory.enum(CUSTOM_LISTS_FEATURE_STATE, NotSet)
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

    override suspend fun get(id: ProductList.Id.Custom): ProductList =
        productListDao
            .select(id = id.backingId)
            .map { customProductListMapper.toDomain(it!!) }
            .flowOn(Dispatchers.IO)
            .first()

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

    override fun all(): Flow<List<ProductList>> =
        productListDao
            .selectAll()
            .map { entities ->
                buildList {
                    add(defaultList())
                    addAll(
                        entities.map { entity ->
                            customProductListMapper.toDomain(entity)
                        }
                    )
                }
            }
            .flowOn(Dispatchers.IO)

    override fun allSummarized(): Flow<List<ProductList.RawSummary>> =
        combine(
            productListDao.defaultListCounters(),
            productListDao.customListsAndCounters(),
            productListDao.stubs(),
        ) {
                defaultListSize,
                customListsAndSizes,
                stubs,
            ->
            buildList {
                add(
                    ProductList.RawSummary(
                        productList = defaultList(),
                        counters = productListCountersMapper.toDomain(defaultListSize),
                        items = stubs[null].orEmpty().map { item ->
                            productListStubMapper.toDomain(item)
                        },
                    )
                )
                addAll(
                    customListsAndSizes.map {
                        val customList = it.customList
                        ProductList.RawSummary(
                            productList = customProductListMapper.toDomain(customList),
                            counters = productListCountersMapper.toDomain(it.counters),
                            items = stubs[customList.id]
                                .orEmpty()
                                .map { item ->
                                    productListStubMapper.toDomain(item)
                                },
                        )
                    }
                )
            }
        }.flowOn(Dispatchers.IO)

    private fun defaultList(): ProductList =
        ProductList(
            id = ProductList.Id.Default,
            title = context.getString(R.string.grocery_list),
            colorScheme = ColorScheme.Yellow,
        )

    override fun customListsFeatureSetting(): Flow<CustomProductListsFeatureSetting> =
        featureState.observe()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun selectedOne(): Flow<ProductList> =
        idOfSelectedOne()
            .flatMapLatest { id ->
                when (id) {
                    is ProductList.Id.Default -> {
                        flowOf(defaultList())
                    }
                    is ProductList.Id.Custom -> {
                        productListDao
                            .select(id = id.backingId)
                            .map { entity ->
                                if (entity == null) {
                                    defaultList()
                                } else {
                                    customProductListMapper.toDomain(entity)
                                }
                            }
                    }
                }
            }

    override suspend fun setFeatureEnabled(enabled: Boolean) {
        val newValue = if (enabled) {
            CustomProductListsFeatureSetting.Enabled
        } else {
            CustomProductListsFeatureSetting.Disabled
        }
        featureState.set(newValue)
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
            featureState.observe(),
            selectedCustomListId.observe(),
        ) { featureState, selectedCustomListId ->
            if (
                selectedCustomListId != null &&
                featureState == CustomProductListsFeatureSetting.Enabled
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

    companion object {
        const val CUSTOM_LISTS_FEATURE_STATE = "app.grocery.list.data.product.list.settings.CUSTOM_LISTS_FEATURE_STATE"
        const val SELECTED_CUSTOM_LIST_ID = "app.grocery.list.data.product.list.settings.SELECTED_CUSTOM_LIST_ID"
    }
}
