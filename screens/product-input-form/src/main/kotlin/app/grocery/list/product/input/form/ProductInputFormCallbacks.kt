package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    CategoryPickerCallbacks {
    fun onComplete()
    fun onProductTitleChange(newValue: String)
    fun onProductInputComplete(productTitle: String, categoryId: Int, payload: Any?)
}


internal object ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onComplete() {}
    override fun onProductTitleChange(newValue: String) {}
    override fun onProductInputComplete(productTitle: String, categoryId: Int, payload: Any?) {}
}
