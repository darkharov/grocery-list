package app.grocery.list.product.list.preview

import app.grocery.list.product.list.preview.elements.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.ProductItemCallbacksMock

internal interface ProductListPreviewCallbacks :
    ProductItemCallbacks {
    fun onAddProduct()
    fun onNext()
}

internal object ProductListPreviewCallbacksMock :
    ProductListPreviewCallbacks,
    ProductItemCallbacks by ProductItemCallbacksMock {
    override fun onAddProduct() {}
    override fun onNext() {}
}
