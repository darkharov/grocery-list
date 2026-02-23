package app.grocery.list.custom.lists.picker

import androidx.compose.runtime.Stable
import app.grocery.list.custom.lists.picker.item.ProductListPickerItemCallbacks
import app.grocery.list.custom.lists.picker.item.ProductListPickerItemCallbacksMock

@Stable
internal interface ProductListPickerCallbacks :
    ProductListPickerItemCallbacks

internal object ProductListPickerCallbacksMock :
    ProductListPickerCallbacks,
    ProductListPickerItemCallbacks by ProductListPickerItemCallbacksMock
