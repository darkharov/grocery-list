package app.grocery.list.main.activity.ui.content

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderNavigation
import app.grocery.list.final_.steps.FinalStepsNavigation
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.preview.ProductListPreviewNavigation
import app.grocery.list.settings.SettingsNavigation

internal class AppNavigationFacade(
    private val backStack: NavBackStack<NavKey>,
) : ProductInputFormNavigation,
    ClearNotificationsReminderNavigation,
    ProductListPreviewNavigation,
    ProductListActionsNavigation,
    FinalStepsNavigation,
    SettingsNavigation {

    override fun goToProductListActions() {
        backStack.add(ProductListActions)
    }

    override fun goToProductEditingForm(productId: Int) {
        backStack.add(ProductInputForm(productId = productId))
    }

    override fun goToFinalSteps() {
        backStack.add(FinalSteps)
    }

    override fun goToNewProductInputForm() {
        backStack.add(ProductInputForm())
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }

    override fun backToListPreview() {
        backStack.retainAll { key -> key is ProductListPreview }
    }

    override fun goToListFormatSettings() {
        backStack.add(ListFormatSettings)
    }

    override fun goToBottomBarSettings() {
        backStack.add(BottomBarSettings)
    }

    override fun goToFaq() {
        backStack.add(Faq)
    }
}
