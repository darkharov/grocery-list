package app.grocery.list.commons.compose.props

import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductListCountersMapper @Inject constructor() {

    fun toPresentation(counters: ProductList.Counters): AppCounterProps {
        val (totalSize, numberOfEnabled) = counters

        return when (totalSize) {
            0 -> {
                AppCounterProps.NoItems
            }
            numberOfEnabled -> {
                AppCounterProps.JustTotalSize(totalSize)
            }
            else -> {
                AppCounterProps.Ratio(
                    numberOfEnabled = numberOfEnabled,
                    totalSize = totalSize,
                )
            }
        }
    }
}
