package app.grocery.list.product.list.preview

import app.grocery.list.domain.Product

interface ProductListPreviewDelegate {
    fun showUndoProductDeletionSnackbar(product: Product)
}
