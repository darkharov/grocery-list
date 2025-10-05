package app.grocery.list.product.list.preview

import androidx.compose.runtime.Stable
import app.grocery.list.product.list.preview.elements.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.ProductItemCallbacksMock

@Stable
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
