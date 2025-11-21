package app.grocery.list.product.input.form

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacks
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerCallbacksMock

@Stable
internal interface ProductInputFormCallbacks :
    CategoryPickerCallbacks {
    fun onProductTitleChange(newValue: TextFieldValue)
    fun onAttemptToCompleteProductInput(
        productTitle: String,
        selectedCategoryId: Int?,
        emoji: EmojiProps?,
        atLeastOneProductJustAdded: Boolean,
    )
    fun onComplete()
}


internal object ProductInputFormCallbacksMock : ProductInputFormCallbacks,
    CategoryPickerCallbacks by CategoryPickerCallbacksMock {
    override fun onProductTitleChange(newValue: TextFieldValue) {}
    override fun onAttemptToCompleteProductInput(
        productTitle: String,
        selectedCategoryId: Int?,
        emoji: EmojiProps?,
        atLeastOneProductJustAdded: Boolean,
    ) {}
    override fun onComplete() {}
}
