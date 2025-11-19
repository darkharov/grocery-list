package app.grocery.list.domain.product

import app.grocery.list.domain.category.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductTitleAndCategoryUseCase @Inject internal constructor(
    private val products: ProductRepository,
    private val categories: CategoryRepository,
) {
    suspend fun execute(productId: Int): ProductTitleAndCategory {
        val (title, categoryId) = products.productTitleAndCategoryId(productId = productId)
        val category = categories.get(id = categoryId)
        return ProductTitleAndCategory(
            productTitle = title,
            category = category,
        )
    }
}
