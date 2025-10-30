package app.grocery.list.domain.list.preview

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.format.ProductTitleFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Singleton
class GetProductListPreviewUseCase @Inject constructor(
    private val repository: AppRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<ProductListPreview> =
        repository
            .categorizedProducts(AppRepository.CategorizedProductsCriteria.All)
            .flatMapLatest { items ->
                when {
                    items.isEmpty() -> {
                        flowOf(ProductListPreview.Empty(repository.templates()))
                    }
                    else -> {
                        repository
                            .productTitleFormatter
                            .observe()
                            .map { formatter ->
                                items(items, formatter)
                            }
                    }
                }
            }

    private fun items(
        items: List<CategoryAndProducts>,
        formatter: ProductTitleFormatter,
    ): ProductListPreview.Items {
        val productCount = items.fold(0) { acc, item ->
            acc + item.products.size
        }
        return ProductListPreview.Items(
            items = items.map { (category, products) ->
                ProductListPreview.Items.Item(
                    category = category.takeIf {
                        items.size >= CategoriesVisibilityCriteria.MIN_NUMBER &&
                        productCount >= CategoriesVisibilityCriteria.MIN_PRODUCT_NUMBER
                    },
                    products = products
                        .map { product ->
                            ProductListPreview.Items.FormattedProduct(
                                productId = product.id,
                                enabled = product.enabled,
                                formattingResult = formatter.print(product),
                            )
                        },
                )
            },
            enableAndDisableAllFeatures = if (
                productCount >= DisableEnableAllVisibilityCriteria.MIN_PRODUCT_NUMBER
            ) {
                ProductListPreview.Items.EnableAndDisableAll(
                    enableAllAvailable = items.any { it.hasDisabledProducts },
                    disableAllAvailable = items.any { it.hasEnabledProducts },
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
