package app.grocery.list.domain.preview

import app.grocery.list.domain.category.CategoryRepository
import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.CategoryProducts
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.template.TemplateRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Singleton
class GetProductListPreviewUseCase @Inject internal constructor(
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
    private val productRepository: ProductRepository,
    private val templateRepository: TemplateRepository,
    private val categoryRepository: CategoryRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<ProductListPreview> =
        productRepository
            .all()
            .flatMapLatest { items ->
                when {
                    items.isEmpty() -> {
                        flowOf(ProductListPreview.Empty(templateRepository.all()))
                    }
                    else -> {
                        getProductTitleFormatter.execute()
                            .map { result ->
                                items(items, result.formatter)
                            }
                    }
                }
            }

    private suspend fun items(
        categoryProducts: List<CategoryProducts>,
        formatter: ProductTitleFormatter,
    ): ProductListPreview.Items {
        val productCount = categoryProducts.fold(0) { acc, item ->
            acc + item.products.size
        }
        return ProductListPreview.Items(
            categories = categoryProducts.map { (categoryId, products) ->
                ProductListPreview.Items.CategoryContent(
                    category = if (
                        categoryProducts.size >= CategoriesVisibilityCriteria.MIN_NUMBER &&
                        productCount >= CategoriesVisibilityCriteria.MIN_PRODUCT_NUMBER
                    ) {
                        categoryRepository.get(categoryId)
                    } else {
                        null
                    },
                    formattedProducts = products
                        .map { product ->
                            ProductListPreview.Items.FormattedProduct(
                                productId = product.id,
                                enabled = product.enabled,
                                title = formatter.print(product),
                            )
                        },
                )
            },
            enableAndDisableAllFeatures = if (
                productCount >= DisableEnableAllVisibilityCriteria.MIN_PRODUCT_NUMBER
            ) {
                ProductListPreview.Items.EnableAndDisableAll(
                    enableAllAvailable = categoryProducts.any { it.hasDisabledProducts },
                    disableAllAvailable = categoryProducts.any { it.hasEnabledProducts },
                )
            } else {
                null
            }
        )
    }

    private object CategoriesVisibilityCriteria {
        const val MIN_NUMBER = 2
        const val MIN_PRODUCT_NUMBER = 5
    }

    private object DisableEnableAllVisibilityCriteria {
        const val MIN_PRODUCT_NUMBER = 3
    }
}
