package app.grocery.list.product.list.preview.elements

internal interface ProductItemCallbacks {
    fun onDelete(productId: Int)
    fun onProductEnabledChange(productId: Int, newValue: Boolean)
}

internal object ProductItemCallbacksMock : ProductItemCallbacks {
    override fun onDelete(productId: Int) {}
    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {}
}
