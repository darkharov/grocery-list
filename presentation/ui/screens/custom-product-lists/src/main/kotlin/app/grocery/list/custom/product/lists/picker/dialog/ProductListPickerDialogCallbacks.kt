package app.grocery.list.custom.product.lists.picker.dialog

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.OnDialogDismiss
import app.grocery.list.commons.compose.OnDialogDismissMock

@Stable
internal interface ProductListPickerDialogCallbacks : OnDialogDismiss {
    fun onDeletionConfirmed(customProductListId: String)
    fun onDeletionRejected(customProductListId: String)
}

internal object ProductListPickerDialogCallbacksMock :
    ProductListPickerDialogCallbacks,
    OnDialogDismiss by OnDialogDismissMock {
    override fun onDeletionRejected(customProductListId: String) {}
    override fun onDeletionConfirmed(customProductListId: String) {}
}
