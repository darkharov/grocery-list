package app.grocery.list.assembly.ui.content

import androidx.navigation.NavDestination
import app.grocery.list.domain.Product
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.product.list.preview.ProductListPreviewDelegate
import app.grocery.list.settings.SettingsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate,
    SettingsDelegate,
    ProductListPreviewDelegate {
    fun handleCurrentDestinationChange(newValue: NavDestination)
    fun undoProductDeletion(product: Product)

}

internal object AppContentDelegateMock : AppContentDelegate {
    override val appVersionName: String = ""
    override fun exitFromApp() {}
    override fun startShopping() {}
    override fun handleCurrentDestinationChange(newValue: NavDestination) {}
    override fun shareProducts(sharingString: String) {}
    override fun contactSupport() {}
    override fun showUndoProductDeletionSnackbar(product: Product) {}
    override fun undoProductDeletion(product: Product) {}
}
