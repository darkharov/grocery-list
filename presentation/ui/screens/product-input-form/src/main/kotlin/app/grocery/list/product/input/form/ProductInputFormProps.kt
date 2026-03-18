package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps

@Immutable
internal data class ProductInputFormProps(
    val productId: Int?,
    val emoji: EmojiProps? = null,
    val enabled: Boolean = true,
    val categoryPicker: CategoryPickerProps = CategoryPickerProps(),
    val atLeastOneProductJustAdded: Boolean = false,
    val addButtonState: AppButtonStateProps = AppButtonStateProps.Gone,
    val doneButtonState: AppButtonStateProps = AppButtonStateProps.Gone,
) {
    val selectedCategoryId get() = categoryPicker.selectedOne?.id
}
