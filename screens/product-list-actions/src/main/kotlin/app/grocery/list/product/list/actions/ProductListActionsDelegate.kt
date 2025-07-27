package app.grocery.list.product.list.actions

import app.grocery.list.domain.Product

interface ProductListActionsDelegate {
    fun onExitFromApp()
    fun onStartShopping()
    fun share(products: List<Product>)
}
