package app.grocery.list.product.list.actions

import app.grocery.list.domain.Product

internal interface ProductListActionsCallbacks {
    fun onGoToClearListConfirmation()
    fun onClearListConfirmed()
    fun onDialogDismiss()
    fun onExitFromApp()
    fun onStartShopping()
    fun onNoEnabledProductsToStartShopping(productCount: Int)
    fun onShare()
    fun onShareAll(dialog: ProductListActionsDialog.SublistToSharePicker)
    fun onShareEnabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker)
    fun onShareDisabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker)
    fun onPaste(text: String)
    fun onReplaceProductsBy(productList: List<Product>)
    fun onAddProducts(productList: List<Product>)
    fun onEnableAllAndStartShopping()
}

internal object ProductListActionsCallbacksMock : ProductListActionsCallbacks {
    override fun onGoToClearListConfirmation() {}
    override fun onClearListConfirmed() {}
    override fun onDialogDismiss() {}
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
    override fun onNoEnabledProductsToStartShopping(productCount: Int) {}
    override fun onShare() {}
    override fun onShareAll(dialog: ProductListActionsDialog.SublistToSharePicker) {}
    override fun onShareEnabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker) {}
    override fun onShareDisabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker) {}
    override fun onPaste(text: String) {}
    override fun onReplaceProductsBy(productList: List<Product>) {}
    override fun onAddProducts(productList: List<Product>) {}
    override fun onEnableAllAndStartShopping() {}
}
