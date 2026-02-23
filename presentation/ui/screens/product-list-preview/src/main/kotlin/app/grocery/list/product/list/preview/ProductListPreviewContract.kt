package app.grocery.list.product.list.preview

import app.grocery.list.domain.product.Product

interface ProductListPreviewContract {
    fun showUndoProductDeletionSnackbar(product: Product)
    fun goToProductEditingForm(productId: Int)
    fun goToProductListsSettings()
}
