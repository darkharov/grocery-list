package app.grocery.list.product.list.actions

import app.grocery.list.commons.compose.AbleToGoBack

interface ProductListActionsContract : AbleToGoBack {
    fun exitFromApp()
    fun startShopping()
    fun goToNewProductInputForm()
    fun goToProductListActions()
}
