package app.grocery.list.product.list.preview.elements.neighbours

import androidx.compose.runtime.Stable

@Stable
internal interface ProductListNeighboursCallbacks {
    fun onNeighbourProductListClick(item: ProductListNeighboursProps.Item)
}

internal object ProductListNeighboursCallbacksMock : ProductListNeighboursCallbacks {
    override fun onNeighbourProductListClick(item: ProductListNeighboursProps.Item) {}
}
