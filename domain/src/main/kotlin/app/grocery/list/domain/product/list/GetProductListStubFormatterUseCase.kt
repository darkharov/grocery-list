package app.grocery.list.domain.product.list

import app.grocery.list.domain.formatter.GetProductTitleFormatterUseCase
import app.grocery.list.domain.formatter.ProductListStubFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class GetProductListStubFormatterUseCase @Inject constructor(
    private val getProductTitleFormatter: GetProductTitleFormatterUseCase,
) {
    fun execute(): Flow<ProductListStubFormatter> =
        getProductTitleFormatter.execute()
            .map { result ->
                result
                    .formatter
                    .withCommas()
                    .toStubFormatter()
            }
}
