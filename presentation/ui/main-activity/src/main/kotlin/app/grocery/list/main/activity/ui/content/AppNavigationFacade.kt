package app.grocery.list.main.activity.ui.content

import androidx.navigation.NavHostController
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderNavigation
import app.grocery.list.final_.steps.FinalSteps
import app.grocery.list.final_.steps.FinalStepsNavigation
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.actions.screen.ProductListActions
import app.grocery.list.product.list.preview.ProductListPreview
import app.grocery.list.product.list.preview.ProductListPreviewNavigation

internal class AppNavigationFacade(
    private val navController: NavHostController,
) : ProductInputFormNavigation,
    ClearNotificationsReminderNavigation,
    ProductListPreviewNavigation,
    ProductListActionsNavigation,
    FinalStepsNavigation {

    override fun goToProductListActions() {
        navController.navigate(ProductListActions)
    }

    override fun goToProductEditingForm(productId: Int) {
        navController.navigate(ProductInputForm(productId = productId))
    }

    override fun goToFinalSteps() {
        navController.navigate(FinalSteps)
    }

    override fun goToNewProductInputForm() {
        navController.navigate(ProductInputForm())
    }

    override fun goBack() {
        navController.popBackStack()
    }

    override fun backToActionsOrListPreview() {
        if (!(navController.popBackStack(ProductListActions, inclusive = false))) {
            navController.popBackStack(ProductListPreview, inclusive = false)
        }
    }
}
