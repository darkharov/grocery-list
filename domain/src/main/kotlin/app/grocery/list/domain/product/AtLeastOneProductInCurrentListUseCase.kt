package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@Singleton
class AtLeastOneProductInCurrentListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val productListRepository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(enabledOnly: Boolean): Flow<Boolean> =
        productListRepository
            .idOfSelectedOne()
            .flatMapLatest { idOfSelectedOne ->
                productRepository
                    .any(
                        Product.Criteria(
                            productListId = idOfSelectedOne,
                            enabledOnly = enabledOnly,
                        )
                    )
            }
}
