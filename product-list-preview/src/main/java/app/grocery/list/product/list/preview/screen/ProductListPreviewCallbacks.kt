package app.grocery.list.product.list.preview.screen

internal interface ProductListPreviewCallbacks {
    fun onDeletedClick(productId: Int)
    fun onNext()
}

internal object ProductListPreviewCallbacksMock : ProductListPreviewCallbacks {
    override fun onDeletedClick(productId: Int) {}
    override fun onNext() {}
}
