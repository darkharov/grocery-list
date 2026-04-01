package app.grocery.list.custom.product.lists.picker.dialog

import androidx.compose.runtime.Stable

@Stable
internal interface ProductListPickerDialogCallbacks {
    fun onDeletionConfirmed(customProductListId: String)
    fun onDeletionRejected(customProductListId: String)
    fun onQuestionDialogClose()
}

internal object ProductListPickerDialogCallbacksMock : ProductListPickerDialogCallbacks {
    override fun onDeletionRejected(customProductListId: String) {}
    override fun onDeletionConfirmed(customProductListId: String) {}
    override fun onQuestionDialogClose() {}
}
