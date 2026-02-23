package app.grocery.list.domain.product.list

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@Singleton
class GetProductListsIfFeatureEnabledUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<ProductListsAndSelection?> =
        productListRepository
            .customListsFeatureSetting()
            .flatMapLatest {
                if (it == CustomProductListsFeatureSetting.Enabled) {
                    combine(
                        productListRepository.selectedOne(),
                        productListRepository.all(),
                        ::ProductListsAndSelection,
                    )
                } else {
                    flowOf(null)
                }
            }
}
