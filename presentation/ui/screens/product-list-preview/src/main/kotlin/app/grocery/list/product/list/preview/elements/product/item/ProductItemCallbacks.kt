package app.grocery.list.product.list.preview.elements.product.item

internal interface ProductItemCallbacks {
    fun onDelete(product: ProductItemProps)
    fun onEditProduct(product: ProductItemProps)
    fun onProductEnabledChange(product: ProductItemProps, newValue: Boolean)
}

internal object ProductItemCallbacksMock : ProductItemCallbacks {
    override fun onDelete(product: ProductItemProps) {}
    override fun onEditProduct(product: ProductItemProps) {}
    override fun onProductEnabledChange(product: ProductItemProps, newValue: Boolean) {}
}
