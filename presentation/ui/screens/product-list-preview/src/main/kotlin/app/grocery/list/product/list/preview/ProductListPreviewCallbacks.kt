package app.grocery.list.product.list.preview

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacks
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogCallbacksMock
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacks
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacksMock
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholderCallbacks
import app.grocery.list.product.list.preview.elements.empty.list.placeholder.EmptyListPlaceholderCallbacksMock
import app.grocery.list.product.list.preview.elements.item.ProductItemCallbacks
import app.grocery.list.product.list.preview.elements.item.ProductItemCallbacksMock
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursCallbacks
import app.grocery.list.product.list.preview.elements.neighbours.ProductListNeighboursCallbacksMock

@Stable
internal interface ProductListPreviewCallbacks :
    EmptyListPlaceholderCallbacks,
    ProductItemCallbacks,
    ConfirmPastedListDialogCallbacks,
    AppQuestionCallbacks,
    ProductListNeighboursCallbacks {
    fun onEnableAll()
    fun onDisableEnableAll()
}

internal object ProductListPreviewCallbacksMock :
    ProductListPreviewCallbacks,
    EmptyListPlaceholderCallbacks by EmptyListPlaceholderCallbacksMock,
    ProductItemCallbacks by ProductItemCallbacksMock,
    ConfirmPastedListDialogCallbacks by ConfirmPastedListDialogCallbacksMock,
    AppQuestionCallbacks by AppQuestionCallbacksMock,
    ProductListNeighboursCallbacks by ProductListNeighboursCallbacksMock {
    override fun onEnableAll() {}
    override fun onDisableEnableAll() {}
}
