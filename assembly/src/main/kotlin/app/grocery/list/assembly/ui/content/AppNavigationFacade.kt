package app.grocery.list.assembly.ui.content

import androidx.navigation.NavHostController
import app.darkharov.clear.notifications.reminder.ClearNotificationsReminderNavigation
import app.grocery.list.final_.steps.FinalSteps
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActions
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.preview.ProductListPreviewNavigation

internal class AppNavigationFacade(
    private val startRoute: Any,
    private val navController: NavHostController,
) : ProductInputFormNavigation,
    ProductListPreviewNavigation,
    ProductListActionsNavigation,
    ClearNotificationsReminderNavigation {

    override fun exitFromProductInputForm() {
        navController.popBackStack()
    }

    override fun goToActions() {
        navController.navigate(ProductListActions)
    }

    override fun goToProductInputForm() {
        navController.navigate(ProductInputForm)
    }

    override fun returnToStartScreen() {
        navController.popBackStack(startRoute, inclusive = false)
    }

    override fun goToFinalSteps() {
        navController.navigate(FinalSteps)
    }
}
