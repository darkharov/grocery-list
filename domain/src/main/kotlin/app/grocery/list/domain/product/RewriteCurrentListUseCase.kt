package app.grocery.list.domain.product

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewriteCurrentListUseCase @Inject constructor(
    private val clearCurrentList: ClearCurrentListUseCase,
    private val productRepository: ProductRepository,
) {
   suspend fun execute(productList: List<Product>) {
        clearCurrentList.execute()
        productRepository.put(productList)
    }
}
