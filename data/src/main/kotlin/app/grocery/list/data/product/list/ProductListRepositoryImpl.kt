package app.grocery.list.data.product.list

import android.content.Context
import app.grocery.list.data.R
import app.grocery.list.domain.product.list.CustomProductListsFeatureState
import app.grocery.list.domain.product.list.CustomProductListsFeatureState.NotSet
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
internal class ProductListRepositoryImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
    private val productListDao: ProductListDao,
    private val productListStubMapper: ProductListStubMapper,
    private val customProductListMapper: CustomProductListMapper,
    storageValueFactory: StorageValueFactory,
) : ProductListRepository {

    private val featureState = storageValueFactory.enum(FEATURE_STATE, NotSet)
    private val selectedCustomListId = storageValueFactory.nullableInt(SELECTED_CUSTOM_LIST_ID)

    override suspend fun put(params: ProductList.CreateParams) {
        withContext(Dispatchers.IO) {
            productListDao.insert(customProductListMapper.toData(params))
        }
    }

    override fun all(): Flow<List<ProductList.RawSummary>> =
        combine(
            productListDao.defaultListSize(),
            productListDao.customListsAndSizes(),
            productListDao.stubs(),
        ) {
                defaultListSize,
                customListsAndSizes,
                stubs,
            ->
            buildList {
                add(
                    ProductList.RawSummary(
                        productList = ProductList(
                            id = ProductList.Id.Default,
                            title = context.getString(R.string.grocery_list),
                            colorScheme = ProductList.ColorScheme.Yellow,
                        ),
                        totalSize = defaultListSize,
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
                            totalSize = it.size,
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

    override fun featureState(): Flow<CustomProductListsFeatureState> =
        featureState.observe()

    override suspend fun setFeatureEnabled(enabled: Boolean) {
        val newValue = if (enabled) {
            CustomProductListsFeatureState.Enabled
        } else {
            CustomProductListsFeatureState.Disabled
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
        selectedCustomListId
            .observe()
            .map { selectedCustomListId ->
                if (selectedCustomListId != null) {
                    ProductList.Id.Custom(backingId = selectedCustomListId)
                } else {
                    ProductList.Id.Default
                }
            }

    companion object {
        const val FEATURE_STATE = "app.grocery.list.data.product.list.settings.FEATURE_STATE"
        const val SELECTED_CUSTOM_LIST_ID = "app.grocery.list.data.product.list.settings.SELECTED_CUSTOM_LIST_ID"
    }
}
