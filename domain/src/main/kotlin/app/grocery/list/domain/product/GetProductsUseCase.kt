package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Singleton
internal class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val productListRepository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(
        criteria: Product.RawCriteria = Product.RawCriteria(),
    ): Flow<List<Product>> =
        criteria
            .listIdStrategy
            .productListId(productListRepository)
            .map { productListId ->
                Product.Criteria(
                    productListId = productListId,
                    enabledOnly = criteria.enabledOnly,
                )
            }.flatMapLatest { criteria ->
                productRepository.get(criteria)
            }
}
