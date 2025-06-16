package app.grocery.list.assembly.ui.content

import app.grocery.list.product.list.actions.ProductListActionsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate {
    fun onScreenChange(route: String)
}

internal object AppContentDelegateMock : AppContentDelegate {
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
    override fun onScreenChange(route: String) {}
}
