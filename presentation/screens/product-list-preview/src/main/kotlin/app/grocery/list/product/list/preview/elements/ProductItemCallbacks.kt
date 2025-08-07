package app.grocery.list.product.list.preview.elements

internal interface ProductItemCallbacks {
    fun onDelete(productId: Int)
}

internal object ProductItemCallbacksMock : ProductItemCallbacks {
    override fun onDelete(productId: Int) {}
}
