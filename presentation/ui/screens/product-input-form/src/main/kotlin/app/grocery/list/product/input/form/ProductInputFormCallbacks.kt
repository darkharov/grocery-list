package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    CategoryPickerCallbacks {
    fun onAttemptToCompleteProductInput(productTitle: String, props: ProductInputFormProps)
    fun onComplete()
}


internal object ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onAttemptToCompleteProductInput(productTitle: String, props: ProductInputFormProps) {}
    override fun onComplete() {}
}
