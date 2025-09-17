package app.grocery.list.product.list.preview

interface ProductListPreviewNavigation {
    fun goToProductInputForm(productId: Int? = null)
    fun goToActions()
}
