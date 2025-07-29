package app.grocery.list.assembly.ui.content

import androidx.navigation.NavDestination
import app.grocery.list.domain.Product
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.settings.SettingsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate,
    SettingsDelegate {
    fun onCurrentDestinationChange(newValue: NavDestination)
}

internal object AppContentDelegateMock : AppContentDelegate {
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
    override fun onCurrentDestinationChange(newValue: NavDestination) {}
    override fun share(products: List<Product>) {}
    override fun contactSupport() {}
}
