package app.grocery.list.assembly.ui.content

import androidx.navigation.NavHostController
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActions
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.preview.ProductListPreview
import app.grocery.list.product.list.preview.ProductListPreviewNavigation

internal class AppNavigationFacade(
    private val navController: NavHostController,
) : ProductInputFormNavigation,
    ProductListPreviewNavigation,
    ProductListActionsNavigation {

    override fun onGoToPreview() {
        navController.navigate(ProductListPreview)
    }

    override fun onGoToActions() {
        navController.navigate(ProductListActions)
    }

    override fun onReturnToInitialScreen() {
        navController.popBackStack(ProductInputForm, false)
    }
}
