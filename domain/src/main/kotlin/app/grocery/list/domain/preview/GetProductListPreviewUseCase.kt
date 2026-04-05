package app.grocery.list.domain.preview

import app.grocery.list.domain.category.CategoryRepository
import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.CategoryProducts
import app.grocery.list.domain.product.GetCategorizedProductsUseCase
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.question.HowToEditProductsQuestion
import app.grocery.list.domain.question.NeedMoreListsQuestion
import app.grocery.list.domain.question.Question
import app.grocery.list.domain.template.TemplateRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@Singleton
class GetProductListPreviewUseCase @Inject internal constructor(
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
    private val getProducts: GetCategorizedProductsUseCase,
    private val templateRepository: TemplateRepository,
    private val categoryRepository: CategoryRepository,
    private val productListRepository: ProductListRepository,
    private val needMoreLists: NeedMoreListsQuestion,
    private val howToEditProducts: HowToEditProductsQuestion,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<ProductListPreview> =
        getProducts
            .execute()
            .flatMapLatest { items ->
                when {
                    items.isEmpty() -> {
                        productListRepository
                            .titleOfCurrentCustomListOrNull()
                            .map { customListTitle ->
                                if (customListTitle != null) {
                                    ProductListPreview.Empty.CustomList(
                                        title = customListTitle,
                                    )
                                } else {
                                    ProductListPreview.Empty.Default(
                                        templates = templateRepository.all(),
                                    )
                                }
                            }
                    }
                    else -> {
                        combine(
                            getProductTitleFormatter.execute(),
                            howToEditProducts.takeIfShouldBeAskedOr { needMoreLists },
                        ) { formatterResult,
                            question ->
                            items(
                                categoryProducts = items,
                                formatter = formatterResult.formatter,
                                question = question,
                            )
                        }
                    }
                }
            }

    private suspend fun items(
        categoryProducts: List<CategoryProducts>,
        formatter: ProductTitleFormatter,
        question: Question?,
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
            },
            question = question,
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
