package app.grocery.list.main.activity.ui.content

import app.grocery.list.domain.product.Product
import app.grocery.list.notifications.NotificationPublisher
import app.grocery.list.product.list.actions.ProductListActionsDelegate
import app.grocery.list.product.list.preview.ProductListPreviewDelegate
import app.grocery.list.settings.SettingsDelegate

internal interface AppContentDelegate :
    ProductListActionsDelegate,
    SettingsDelegate,
    ProductListPreviewDelegate {
    val notificationPublisher: NotificationPublisher
    fun undoProductDeletion(product: Product)
    fun openNotificationSettings()
    fun handleDialogDismiss()
}

internal object AppContentDelegateMock : AppContentDelegate {
    override val appVersionName: String = ""
    override val appVersionCode = 1
    override val notificationPublisher get() = throw UnsupportedOperationException()
    override fun exitFromApp() {}
    override fun startShopping() {}
    override fun showUndoProductDeletionSnackbar(product: Product) {}
    override fun undoProductDeletion(product: Product) {}
    override fun openNotificationSettings() {}
    override fun handleDialogDismiss() {}
}
