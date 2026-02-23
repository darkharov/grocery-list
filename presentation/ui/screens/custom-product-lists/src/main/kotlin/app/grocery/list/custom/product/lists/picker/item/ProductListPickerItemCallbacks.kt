package app.grocery.list.custom.product.lists.picker.item

import androidx.compose.runtime.Stable

@Stable
internal interface ProductListPickerItemCallbacks {
    fun onSelect(item: ProductListPickerItemProps)
    fun onEdit(item: ProductListPickerItemProps)
    fun onDelete(item: ProductListPickerItemProps)
}

internal object ProductListPickerItemCallbacksMock : ProductListPickerItemCallbacks {
    override fun onSelect(item: ProductListPickerItemProps) {}
    override fun onEdit(item: ProductListPickerItemProps) {}
    override fun onDelete(item: ProductListPickerItemProps) {}
}
