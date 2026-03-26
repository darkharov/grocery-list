package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class DisableAllProductsInCurrentListUseCase @Inject internal constructor(
    private val productRepository: ProductRepository,
    private val productListRepository: ProductListRepository,
) {
    suspend fun execute() {
        val listId = productListRepository.idOfSelectedOne().first()
        productRepository.disableAll(listId)
    }
}
