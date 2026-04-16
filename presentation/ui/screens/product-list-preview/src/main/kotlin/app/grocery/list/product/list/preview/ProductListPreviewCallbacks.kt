package app.grocery.list.product.list.preview

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacks
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacksMock
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacks
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacksMock
import app.grocery.list.product.list.preview.elements.item.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.item.ProductItemCallbacksMock
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursCallbacks
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursCallbacksMock

@Stable
internal interface ProductListPreviewCallbacks :
    ProductItemCallbacks,
    ConfirmPastedListDialogCallbacks,
    AppQuestionCallbacks,
    ProductListNeighboursCallbacks {
    fun onEnableAll()
    fun onDisableEnableAll()
    fun onTemplateClick(template: ProductListPreviewProps.Empty.Template)
}

internal object ProductListPreviewCallbacksMock :
    ProductListPreviewCallbacks,
    ProductItemCallbacks by ProductItemCallbacksMock,
    ConfirmPastedListDialogCallbacks by ConfirmPastedListDialogCallbacksMock,
    AppQuestionCallbacks by AppQuestionCallbacksMock,
    ProductListNeighboursCallbacks by ProductListNeighboursCallbacksMock {
    override fun onEnableAll() {}
    override fun onDisableEnableAll() {}
    override fun onTemplateClick(template: ProductListPreviewProps.Empty.Template) {}
}
