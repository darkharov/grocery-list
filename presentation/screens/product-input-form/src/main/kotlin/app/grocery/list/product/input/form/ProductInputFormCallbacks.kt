package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    CategoryPickerCallbacks {
    fun onComplete()
    fun onProductTitleChange(newValue: TextFieldValue)
    fun onProductInputComplete(productTitle: String, categoryId: Int, emoji: EmojiProps?)
}


internal object ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onComplete() {}
    override fun onProductTitleChange(newValue: TextFieldValue) {}
    override fun onProductInputComplete(productTitle: String, categoryId: Int, emoji: EmojiProps?) {}
}
