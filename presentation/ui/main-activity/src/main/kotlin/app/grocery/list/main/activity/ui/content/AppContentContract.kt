package app.grocery.list.main.activity.ui.content

import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderContract
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarCallbacks
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarCallbacksMock
import app.grocery.list.domain.product.Product
import app.grocery.list.final_.steps.FinalStepsContract
import app.grocery.list.product.input.form.ProductInputFormContract
import app.grocery.list.product.list.actions.ProductListActionsContract
import app.grocery.list.product.list.preview.ProductListPreviewContract
import app.grocery.list.settings.SettingsContract
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

internal interface AppContentContract :
    AppToolbarCallbacks,
    ProductListActionsContract,
    SettingsContract,
    ProductListPreviewContract,
    ClearNotificationsReminderContract,
    FinalStepsContract,
    ProductInputFormContract {
    fun snackbars(): ReceiveChannel<AppSnackbar>
    fun undoProductDeletion(product: Product)
    fun openNotificationSettings()
    fun dismissDialog()
}

internal object AppContentContractMock :
    AppContentContract,
    AppToolbarCallbacks by AppToolbarCallbacksMock {
    override fun snackbars(): ReceiveChannel<AppSnackbar> = Channel()
    override fun exitFromApp() {}
    override fun startShopping() {}
    override fun showUndoProductDeletionSnackbar(product: Product) {}
    override fun undoProductDeletion(product: Product) {}
    override fun openNotificationSettings() {}
    override fun dismissDialog() {}
    override fun goToNewProductInputForm() {}
    override fun goToProductListActions() {}
    override fun goBack() {}
    override fun goToListFormatSettings() {}
    override fun goToBottomBarSettings() {}
    override fun goToFaq() {}
    override fun goToProductEditingForm(productId: Int) {}
    override fun goToFinalSteps() {}
    override fun backToListPreview() {}
}
