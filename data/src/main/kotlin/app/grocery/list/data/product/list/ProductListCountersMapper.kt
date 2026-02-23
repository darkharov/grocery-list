package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListCountersMapper @Inject constructor() {

    fun toDomain(counters: ProductListCountersQuery): ProductList.Counters =
        ProductList.Counters(
            totalSize = counters.totalSize,
            numberOfEnabled = counters.numberOfEnabledItems,
        )
}
