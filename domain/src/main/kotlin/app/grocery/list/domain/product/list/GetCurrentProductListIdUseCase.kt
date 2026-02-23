package app.grocery.list.domain.product.list

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class GetCurrentProductListIdUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
) {
    fun execute(): Flow<ProductList.Id> =
        combine(
            productListRepository.idOfSelectedOne(),
            productListRepository.featureState(),
        ) {
                selectedOne,
                featureState,
            ->
            if (featureState == CustomProductListsFeatureState.Enabled) {
                selectedOne
            } else {
                ProductList.Id.Default
            }
        }
}
