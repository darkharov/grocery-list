package app.grocery.list.product.list.preview

import app.grocery.list.domain.product.Product

interface ProductListPreviewDelegate {
    fun showUndoProductDeletionSnackbar(product: Product)
}
