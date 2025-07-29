package app.grocery.list.assembly.ui.content

import androidx.navigation.NavDestination
import app.grocery.list.domain.Product
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.settings.SettingsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate,
    SettingsDelegate {
    fun handleCurrentDestinationChange(newValue: NavDestination)
}

internal object AppContentDelegateMock : AppContentDelegate {
    override fun exitFromApp() {}
    override fun startShopping() {}
    override fun handleCurrentDestinationChange(newValue: NavDestination) {}
    override fun share(products: List<Product>) {}
    override fun contactSupport() {}
}
