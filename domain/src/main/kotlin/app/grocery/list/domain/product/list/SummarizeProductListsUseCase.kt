package app.grocery.list.domain.product.list

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class SummarizeProductListsUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val getProductListStubFormatter: GetProductListStubFormatterUseCase,
) {
    fun execute(): Flow<List<ProductList.Summary>> =
        combine(
            productListRepository.allSummarized(),
            productListRepository.idOfSelectedOne(),
            getProductListStubFormatter.execute(),
        ) {
                all,
                idOfSelectedOne,
                formatter,
            ->
            all.map { rawSummary ->
                ProductList.Summary(
                    productList = rawSummary.productList,
                    counters = rawSummary.counters,
                    formattedStub = formatter.print(rawSummary),
                    isSelected = (rawSummary.productList.id == idOfSelectedOne),
                )
            }
        }
}
