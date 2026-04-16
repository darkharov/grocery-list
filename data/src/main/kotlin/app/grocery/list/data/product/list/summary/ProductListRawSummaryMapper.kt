package app.grocery.list.data.product.list.summary

import app.grocery.list.data.product.list.summary.content.stub.ProductListContentStubMapper
import app.grocery.list.data.product.list.summary.content.stub.ProductListContentStubQuery
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListRawSummaryMapper @Inject constructor(
    private val productListWithCountersMapper: ProductListWithCountersMapper,
    private val productListContentStubMapper: ProductListContentStubMapper,
) {
    fun toDomain(params: Params): List<ProductList.RawSummary> =
        params.listsWithCounters.map { listWithCounters ->
            val optionalCustomListId = listWithCounters.optionalCustomProductList.id
            val contentStub = params.contentStubs[optionalCustomListId].orEmpty()
            ProductList.RawSummary(
                items = contentStub.map { item ->
                    productListContentStubMapper.toDomain(item)
                },
                listWithCounters = productListWithCountersMapper.toDomain(listWithCounters),
            )
        }

    data class Params(
        val contentStubs: Map<Int?, List<ProductListContentStubQuery>>,
        val listsWithCounters: List<ProductListWithCountersView>,
    )
}
