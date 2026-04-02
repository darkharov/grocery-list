package app.grocery.list.domain.theming

import app.grocery.list.domain.product.GetNumberOfProductsInSelectedListUseCase
import app.grocery.list.domain.product.list.CustomProductListsSetting
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class GetAppTopLevelParamsUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val settingsRepository: SettingsRepository,
    private val getNumberOfProducts: GetNumberOfProductsInSelectedListUseCase,
) {
    fun execute(): Flow<AppTopLevelParams> =
        combine(
            productListRepository.selectedOne(),
            productListRepository.customListsSetting(),
            getNumberOfProducts.execute(enabledOnly = true),
            settingsRepository.productTitleFormat.observe(),
        ) {
                currentProductList,
                customListsFeatureSetting,
                numberOfEnabledProducts,
                productTitleFormat,
            ->
            AppTopLevelParams(
                currentProductList = currentProductList,
                productTitleFormat = productTitleFormat,
                numberOfEnabledProducts = numberOfEnabledProducts,
                customListsFeatureEnabled =
                    (customListsFeatureSetting == CustomProductListsSetting.Enabled),
            )
        }
}
