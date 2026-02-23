package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.GetCurrentProductListIdUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Singleton
internal class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val getCurrentProductListId: GetCurrentProductListIdUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(enabledOnly: Boolean = false): Flow<List<Product>> =
        getCurrentProductListId
            .execute()
            .map { productListId ->
                Product.Criteria(
                    productListId = productListId,
                    enabledOnly = enabledOnly,
                )
            }
            .flatMapLatest { criteria ->
                productRepository.get(criteria)
            }
}
