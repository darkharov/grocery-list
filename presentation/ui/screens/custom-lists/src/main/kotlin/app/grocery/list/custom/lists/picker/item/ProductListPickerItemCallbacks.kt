package app.grocery.list.custom.lists.picker.item

import androidx.compose.runtime.Stable

@Stable
internal interface ProductListPickerItemCallbacks {
    fun onSelect(item: ProductListPickerItemProps)
}

internal object ProductListPickerItemCallbacksMock : ProductListPickerItemCallbacks {
    override fun onSelect(item: ProductListPickerItemProps) {}
}
