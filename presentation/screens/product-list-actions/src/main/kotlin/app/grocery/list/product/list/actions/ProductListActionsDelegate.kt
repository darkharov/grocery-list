package app.grocery.list.product.list.actions

interface ProductListActionsDelegate {
    fun exitFromApp()
    fun startShopping()
    fun shareProducts(sharingString: String)
}
