package app.grocery.list.product.list.preview.elements

import app.grocery.list.product.list.preview.ProductListPreviewProps

internal interface ProductItemCallbacks {
    fun onDelete(product: ProductListPreviewProps.Product)
    fun onProductEnabledChange(productId: Int, newValue: Boolean)
}

internal object ProductItemCallbacksMock : ProductItemCallbacks {
    override fun onDelete(product: ProductListPreviewProps.Product) {}
    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {}
}
