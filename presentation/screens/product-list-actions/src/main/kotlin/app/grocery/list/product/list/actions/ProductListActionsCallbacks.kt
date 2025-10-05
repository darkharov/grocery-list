package app.grocery.list.product.list.actions

import androidx.compose.runtime.Stable
import app.grocery.list.domain.Product
import app.grocery.list.product.list.actions.dialog.ProductListActionsDialogProps

@Stable
internal interface ProductListActionsCallbacks {
    fun onAttemptToClearList()
    fun onAdd()
    fun onGoToActions()
    fun onClearListConfirmed()
    fun onDialogDismiss()
    fun onExitFromApp()
    fun onAttemptToStartShopping(atLeastOneProductEnabled: Boolean, numberOfProducts: Int)
    fun onNoEnabledProductsToStartShopping(productCount: Int)
    fun onAttemptToShareCurrentList()
    fun onShareAll(dialog: ProductListActionsDialogProps.SublistToSharePicker)
    fun onShareEnabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker)
    fun onShareDisabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker)
    fun onAttemptToPaste()
    fun onPasted(text: String)
    fun onReplaceProductsBy(productList: List<Product>)
    fun onAddProducts(productList: List<Product>)
    fun onEnableAllAndStartShopping()
}

internal object ProductListActionsCallbacksMock : ProductListActionsCallbacks {
    override fun onAttemptToClearList() {}
    override fun onAdd() {}
    override fun onGoToActions() {}
    override fun onClearListConfirmed() {}
    override fun onDialogDismiss() {}
    override fun onExitFromApp() {}
    override fun onAttemptToStartShopping(atLeastOneProductEnabled: Boolean, numberOfProducts: Int) {}
    override fun onNoEnabledProductsToStartShopping(productCount: Int) {}
    override fun onAttemptToShareCurrentList() {}
    override fun onShareAll(dialog: ProductListActionsDialogProps.SublistToSharePicker) {}
    override fun onShareEnabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {}
    override fun onShareDisabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {}
    override fun onAttemptToPaste() {}
    override fun onPasted(text: String) {}
    override fun onReplaceProductsBy(productList: List<Product>) {}
    override fun onAddProducts(productList: List<Product>) {}
    override fun onEnableAllAndStartShopping() {}
}
