package app.grocery.list.product.list.actions

internal interface ProductListActionsCallbacks {
    fun onGoToClearListConfirmation()
    fun onClearListConfirmed()
    fun onClearListDenied()
    fun onExitFromApp()
    fun onStartShopping()
}

internal object ProductListActionsCallbacksMock : ProductListActionsCallbacks {
    override fun onGoToClearListConfirmation() {}
    override fun onClearListConfirmed() {}
    override fun onClearListDenied() {}
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
}
