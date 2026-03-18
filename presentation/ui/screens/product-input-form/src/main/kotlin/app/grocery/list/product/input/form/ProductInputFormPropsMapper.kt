package app.grocery.list.product.input.form

import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.domain.category.Category
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.product.input.form.elements.category.picker.CategoryMapper
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductInputFormPropsMapper @Inject constructor(
    private val emojiMapper: EmojiMapper,
    private val categoryMapper: CategoryMapper,
) {
    fun transform(params: Params) =
        with(params) {
            ProductInputFormProps(
                productId = productId,
                emoji = emojiMapper.toPresentationNullable(currentOrSuggestedEmoji),
                enabled = enabled,
                categoryPicker = CategoryPickerProps(
                    items = categoryMapper.transformList(categories),
                    expanded = categoryPickerExpanded,
                    selectedOne = categoryMapper.transformNullable(selectedOrSuggestedCategory),
                ),
                atLeastOneProductJustAdded = atLeastOneProductJustAdded,
                addButtonState = when {
                    (productId != null) -> AppButtonStateProps.Gone
                    (title.isBlank()) -> AppButtonStateProps.Disabled
                    else -> AppButtonStateProps.Normal
                },
                doneButtonState = if (productId == null) {
                    if (title.isBlank() && atLeastOneProductJustAdded) {
                        AppButtonStateProps.Normal
                    } else {
                        AppButtonStateProps.Disabled
                    }
                } else {
                    if (title.isBlank()) {
                        AppButtonStateProps.Disabled
                    } else {
                        AppButtonStateProps.Normal
                    }
                },
            )
        }

    data class Params(
        val productId: Int?,
        val enabled: Boolean,
        val currentOrSuggestedEmoji: EmojiAndKeyword?,
        val categories: List<Category>,
        val selectedOrSuggestedCategory: Category?,
        val categoryPickerExpanded: Boolean,
        val atLeastOneProductJustAdded: Boolean,
        val title: CharSequence,
    )
}
