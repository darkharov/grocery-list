package app.grocery.list.assembly.ui.content

import androidx.navigation.NavHostController
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderNavigation
import app.grocery.list.final_.steps.FinalSteps
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActions
import app.grocery.list.product.list.preview.ProductListPreviewNavigation

internal class AppNavigationFacade(
    private val navController: NavHostController,
) : ProductInputFormNavigation,
    ClearNotificationsReminderNavigation,
    ProductListPreviewNavigation {

    override fun exitFromProductInputForm() {
        navController.popBackStack()
    }

    override fun goToActions() {
        navController.navigate(ProductListActions)
    }

    override fun goToProductInputForm(productId: Int?) {
        navController.navigate(ProductInputForm(productId = productId))
    }

    override fun goToFinalSteps() {
        navController.navigate(FinalSteps)
    }
}
