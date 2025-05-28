package app.grocery.list.product.list.actions.screen

internal interface ProductListActionsCallbacks {
    fun onClearList()
    fun onExitFromApp()
    fun onStartShopping()
}

internal object ProductListActionsCallbacksMock : ProductListActionsCallbacks {
    override fun onClearList() {}
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
}
