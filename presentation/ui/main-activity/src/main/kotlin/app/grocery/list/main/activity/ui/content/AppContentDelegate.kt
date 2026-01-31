package app.grocery.list.main.activity.ui.content

import androidx.navigation3.runtime.NavKey
import app.grocery.list.domain.product.Product
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.product.list.preview.ProductListPreviewDelegate
import app.grocery.list.settings.SettingsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate,
    SettingsDelegate,
    ProductListPreviewDelegate {
    fun handleCurrentScreenChange(newValue: NavKey)
    fun undoProductDeletion(product: Product)
    fun openNotificationSettings()
    fun handleDialogDismiss()
}

internal object AppContentDelegateMock : AppContentDelegate {
    override val appVersionName: String = ""
    override fun exitFromApp() {}
    override fun startShopping() {}
    override fun handleCurrentScreenChange(newValue: NavKey) {}
    override fun shareProducts(sharingString: String) {}
    override fun contactSupport() {}
    override fun showUndoProductDeletionSnackbar(product: Product) {}
    override fun undoProductDeletion(product: Product) {}
    override fun openNotificationSettings() {}
    override fun handleDialogDismiss() {}
}
