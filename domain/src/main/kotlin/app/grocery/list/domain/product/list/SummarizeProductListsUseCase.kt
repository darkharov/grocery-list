package app.grocery.list.domain.product.list

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class SummarizeProductListsUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
) {
    fun execute(): Flow<List<ProductList.FormattedSummary>> =
        combine(
            getProductTitleFormatter.execute(),
            productListRepository.all(),
        ) { formatterResult, rawSummaries ->
            rawSummaries.map { rawSummary ->
                ProductList.FormattedSummary(
                    productList = rawSummary.productList,
                    stub = formatterResult
                        .formatter
                        .withCommas()
                        .print(rawSummary.sampleItems),
                    totalSize = rawSummary.totalSize,
                )
            }
        }
}
