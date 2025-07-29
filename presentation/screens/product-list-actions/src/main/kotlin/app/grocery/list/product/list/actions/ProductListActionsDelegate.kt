package app.grocery.list.product.list.actions

import app.grocery.list.domain.Product

interface ProductListActionsDelegate {
    fun exitFromApp()
    fun startShopping()
    fun share(products: List<Product>)
}
