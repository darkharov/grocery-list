package app.grocery.list.domain.product

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class GetCategorizedProductsUseCase @Inject constructor(
    private val getProducts: GetProductsUseCase,
) {
    fun execute(
        criteria: Product.RawCriteria = Product.RawCriteria(),
    ): Flow<List<CategoryProducts>> =
        getProducts
            .execute(criteria)
            .map { products ->
                products
                    .groupBy { it.categoryId }
                    .map { CategoryProducts(it.key, it.value) }
            }
}
