package app.grocery.list.domain.product

import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

@Singleton
class ClearCurrentListUseCase @Inject internal constructor(
    private val productRepository: ProductRepository,
    private val productListRepository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun execute() {
        val id = productListRepository.idOfSelectedOne().first()
        productRepository.deleteAll(id)
    }
}
