package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps

@Immutable
internal data class ProductInputFormProps(
    val state: State = State.Initial,
    val emoji: EmojiProps? = null,
    val enabled: Boolean = true,
    val categoryPicker: CategoryPickerProps = CategoryPickerProps(),
    val atLeastOneProductJustAdded: Boolean = false,
) {
    val productId get() = (state as? State.Editing)?.productId
    val selectedCategoryId get() = categoryPicker.selectedOne?.id

    constructor(
        productId: Int?,
        emoji: EmojiProps?,
        enabled: Boolean,
        categoryPicker: CategoryPickerProps,
        atLeastOneProductJustAdded: Boolean,
    ) : this(
        state = if (productId != null) {
            State.Editing(productId = productId)
        } else {
            State.Adding
        },
        emoji = emoji,
        enabled = enabled,
        categoryPicker = categoryPicker,
        atLeastOneProductJustAdded = atLeastOneProductJustAdded,
    )

    @Immutable
    sealed class State {

        @Immutable
        data object Initial : State()

        @Immutable
        data object Adding : State()

        @Immutable
        data class Editing(val productId: Int) : State()
    }
}
