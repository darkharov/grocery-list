package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.CustomListsSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Singleton
internal class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val customListsSettingsRepository: CustomListsSettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(enabledOnly: Boolean = false): Flow<List<Product>> =
        customListsSettingsRepository
            .idOfSelectedCustomList()
            .map { idOfSelectedCustomList ->
                ProductsCriteria(
                    enabledOnly = enabledOnly,
                    idOfSelectedCustomList = idOfSelectedCustomList,
                )
            }
            .flatMapLatest(productRepository::get)
}
