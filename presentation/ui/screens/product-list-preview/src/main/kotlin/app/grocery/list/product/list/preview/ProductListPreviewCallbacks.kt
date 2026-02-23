package app.grocery.list.product.list.preview

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacks
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacksMock
import app.grocery.list.product.list.preview.elements.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.ProductItemCallbacksMock

@Stable
internal interface ProductListPreviewCallbacks :
    ProductItemCallbacks,
    ConfirmPastedListDialogCallbacks {
    fun onEnableAll()
    fun onDisableEnableAll()
    fun onTemplateClick(template: ProductListPreviewProps.Empty.Template)
    fun onNeedMoreListsClick()
}

internal object ProductListPreviewCallbacksMock :
    ProductListPreviewCallbacks,
    ProductItemCallbacks by ProductItemCallbacksMock,
    ConfirmPastedListDialogCallbacks by ConfirmPastedListDialogCallbacksMock {
    override fun onEnableAll() {}
    override fun onDisableEnableAll() {}
    override fun onTemplateClick(template: ProductListPreviewProps.Empty.Template) {}
    override fun onNeedMoreListsClick() {}
}
