package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPickerCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    CategoryPickerCallbacks {
    fun onReadyToGoToPreview()
    fun onProductTitleChange(newValue: String)
    fun onProductInputComplete(productTitle: String, categoryId: Int)
}


internal open class ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onReadyToGoToPreview() {}
    override fun onProductTitleChange(newValue: String) {}
    override fun onProductInputComplete(productTitle: String, categoryId: Int) {}
}
