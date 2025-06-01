package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryPickerCallbacksMock
import app.grocery.list.product.input.form.screen.elements.title.input.ProductTitleInputCallbacks
import app.grocery.list.product.input.form.screen.elements.title.input.ProductTitleInputCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    ProductTitleInputCallbacks,
    CategoryPickerCallbacks {
    fun onReadyToGoToPreview()
    fun onProductInputComplete(productTitle: String, categoryId: Int)
}


internal open class ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    ProductTitleInputCallbacks by ProductTitleInputCallbacksMock,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onReadyToGoToPreview() {}
    override fun onProductInputComplete(productTitle: String, categoryId: Int) {}
}
