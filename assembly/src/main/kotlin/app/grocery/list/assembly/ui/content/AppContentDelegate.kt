package app.grocery.list.assembly.ui.content

import androidx.navigation.NavDestination
import app.grocery.list.product.list.actions.ProductListActionsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate {
    fun onCurrentDestinationChange(newValue: NavDestination)
}

internal object AppContentDelegateMock : AppContentDelegate {
    override fun onExitFromApp() {}
    override fun onStartShopping() {}
    override fun onCurrentDestinationChange(newValue: NavDestination) {}
}
