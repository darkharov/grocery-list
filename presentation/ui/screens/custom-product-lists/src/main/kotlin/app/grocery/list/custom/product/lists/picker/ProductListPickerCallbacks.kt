package app.grocery.list.custom.product.lists.picker

import androidx.compose.runtime.Stable
import app.grocery.list.custom.product.lists.picker.dialog.ProductListPickerDialogCallbacks
import app.grocery.list.custom.product.lists.picker.dialog.ProductListPickerDialogCallbacksMock
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemCallbacks
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemCallbacksMock

@Stable
internal interface ProductListPickerCallbacks :
    ProductListPickerItemCallbacks,
    ProductListPickerDialogCallbacks {
    fun onAddClick()
}

internal object ProductListPickerCallbacksMock :
    ProductListPickerCallbacks,
    ProductListPickerItemCallbacks by ProductListPickerItemCallbacksMock,
    ProductListPickerDialogCallbacks by ProductListPickerDialogCallbacksMock {
    override fun onAddClick() {}
}
