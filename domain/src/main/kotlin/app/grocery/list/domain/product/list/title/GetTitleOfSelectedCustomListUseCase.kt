package app.grocery.list.domain.product.list.title

import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@Singleton
class GetTitleOfSelectedCustomListUseCase @Inject constructor(
    private val repository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun execute(): String? =
        repository
            .idOfSelectedOne()
            .flatMapLatest { idOfSelectedOne ->
                when (idOfSelectedOne) {
                    is ProductList.Id.Custom -> {
                        repository.title(
                            productListId = idOfSelectedOne
                        )
                    }
                    is ProductList.Id.Default -> {
                        flowOf(null)
                    }
                }
            }
            .first()
}
