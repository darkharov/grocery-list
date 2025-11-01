package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.OnDialogDismiss
import app.grocery.list.domain.product.Product

@Stable
interface ConfirmPastedListDialogCallbacks : OnDialogDismiss {
    fun onPasteProductsConfirmed(products: List<Product>)
}

object ConfirmPastedListDialogCallbacksMock : ConfirmPastedListDialogCallbacks {
    override fun onDialogDismiss() {}
    override fun onPasteProductsConfirmed(products: List<Product>) {}
}
