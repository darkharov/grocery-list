package app.grocery.list.product.list.preview

import app.grocery.list.product.list.preview.elements.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.ProductItemCallbacksMock

internal interface ProductListPreviewCallbacks :
    ProductItemCallbacks {
    fun onEnableAll()
    fun onDisableEnableAll()
}

internal object ProductListPreviewCallbacksMock :
    ProductListPreviewCallbacks,
    ProductItemCallbacks by ProductItemCallbacksMock {
    override fun onEnableAll() {}
    override fun onDisableEnableAll() {}
}
