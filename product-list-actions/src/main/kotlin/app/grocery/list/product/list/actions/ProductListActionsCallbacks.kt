package app.grocery.list.product.list.actions

internal interface ProductListActionsCallbacks {
    fun onClearListOptionSelected()
    fun onExitOptionSelected()
    fun onStartShoppingSelected()
}

internal object ProductListActionsCallbacksMock : ProductListActionsCallbacks {
    override fun onClearListOptionSelected() {}
    override fun onExitOptionSelected() {}
    override fun onStartShoppingSelected() {}
}
