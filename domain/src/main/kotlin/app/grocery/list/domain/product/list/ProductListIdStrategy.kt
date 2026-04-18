package app.grocery.list.domain.product.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

sealed interface ProductListIdStrategy {

    fun productListId(repository: ProductListRepository): Flow<ProductList.Id>

    data class SpecificOne(
        private val id: ProductList.Id,
    ) : ProductListIdStrategy {

        override fun productListId(repository: ProductListRepository): Flow<ProductList.Id> =
            flowOf(id)
    }

    data object CurrentSelection : ProductListIdStrategy {

        override fun productListId(repository: ProductListRepository): Flow<ProductList.Id> =
            repository.idOfSelectedOne()
    }
}
