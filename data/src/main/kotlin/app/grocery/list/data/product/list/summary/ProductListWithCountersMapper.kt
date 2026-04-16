package app.grocery.list.data.product.list.summary

import app.grocery.list.data.product.list.summary.counters.ProductListCountersMapper
import app.grocery.list.data.product.list.summary.optional.custom.list.OptionalCustomProductListMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListWithCountersMapper @Inject constructor(
    private val optionalCustomProductListMapper: OptionalCustomProductListMapper,
    private val productListCountersMapper: ProductListCountersMapper,
) {
    fun toDomain(productListWithCounters: ProductListWithCountersView): ProductList.WithCounters {
        val (optionalCustomProductList, counters) = productListWithCounters
        return ProductList.WithCounters(
            counters = productListCountersMapper.toDomain(counters),
            productList = optionalCustomProductListMapper.toDomain(optionalCustomProductList),
        )
    }
}
