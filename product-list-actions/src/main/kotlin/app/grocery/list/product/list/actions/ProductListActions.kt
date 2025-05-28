package app.grocery.list.product.list.actions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.product.list.actions.screen.ProductListActionsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProductListActions

interface ProductListActionsNavigation {
    fun onListCleared()
    fun onExitFromApp()
    fun onStartShopping()
}

fun NavGraphBuilder.productListActionsScreen(
    navigation: ProductListActionsNavigation,
) {
    composable<ProductListActions> {
        ProductListActionsScreen(
            navigation = navigation,
        )
    }
}
