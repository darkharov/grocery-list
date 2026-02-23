package app.grocery.list.custom.product.lists.picker

import app.grocery.list.domain.product.list.ProductList

interface ProductListPickerContract {
    fun goToCustomProductListInputForm(customListId: ProductList.Id.Custom? = null)
}
