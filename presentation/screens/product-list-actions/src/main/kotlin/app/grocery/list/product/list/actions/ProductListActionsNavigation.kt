package app.grocery.list.product.list.actions

import app.grocery.list.commons.compose.AbleToGoBack

interface ProductListActionsNavigation : AbleToGoBack {
    fun goToNewProductInputForm()
    fun goToProductListActions()
}
