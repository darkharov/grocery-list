package app.grocery.list.assembly.ui.content

import app.grocery.list.product.list.actions.ProductListActionsDelegate

internal interface AppDelegatesFacade :
    ProductListActionsDelegate

internal object AppDelegatesFacadeMock : AppDelegatesFacade {
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
}
