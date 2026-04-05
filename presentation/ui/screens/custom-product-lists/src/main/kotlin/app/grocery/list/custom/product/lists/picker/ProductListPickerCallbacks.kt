package app.grocery.list.custom.product.lists.picker

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacks
import app.grocery.list.commons.compose.elements.question.AppQuestionCallbacksMock
import app.grocery.list.custom.product.lists.picker.dialog.ProductListPickerDialogCallbacks
import app.grocery.list.custom.product.lists.picker.dialog.ProductListPickerDialogCallbacksMock
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemCallbacks
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemCallbacksMock

@Stable
internal interface ProductListPickerCallbacks :
    ProductListPickerItemCallbacks,
    ProductListPickerDialogCallbacks,
    AppQuestionCallbacks {
    fun onAddClick()
}

internal object ProductListPickerCallbacksMock :
    ProductListPickerCallbacks,
    ProductListPickerItemCallbacks by ProductListPickerItemCallbacksMock,
    ProductListPickerDialogCallbacks by ProductListPickerDialogCallbacksMock,
    AppQuestionCallbacks by AppQuestionCallbacksMock {
    override fun onAddClick() {}
}
