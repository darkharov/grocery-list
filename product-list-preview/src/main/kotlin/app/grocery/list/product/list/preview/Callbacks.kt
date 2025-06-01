package app.grocery.list.product.list.preview

internal interface ProductListPreviewCallbacks {
    fun onDelete(productId: Int)
    fun onNext()
}

internal object ProductListPreviewCallbacksMock : ProductListPreviewCallbacks {
    override fun onDelete(productId: Int) {}
    override fun onNext() {}
}
