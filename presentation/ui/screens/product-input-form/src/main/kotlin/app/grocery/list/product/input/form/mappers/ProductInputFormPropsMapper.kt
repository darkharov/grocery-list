package app.grocery.list.product.input.form.mappers

import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuMapper
import app.grocery.list.domain.category.Category
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.product.input.form.ProductInputFormProps
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductInputFormPropsMapper @Inject constructor(
    private val emojiMapper: EmojiMapper,
    private val categoriesMapper: AppDropdownMenuMapper<Category>,
    private val productListsMapper: AppDropdownMenuMapper<ProductList>,
) {
    fun transform(params: Params): ProductInputFormProps =
        with(params) {
            ProductInputFormProps(
                productId = productId,
                emoji = emojiMapper.toPresentationNullable(currentOrSuggestedEmoji),
                enabled = enabled,
                categoriesDropdown = categoriesMapper.toPresentation(categories),
                productListsDropdown = if (productLists != null) {
                    productListsMapper.toPresentation(productLists)
                } else {
                    null
                },
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
        val categories: AppDropdownMenuMapper.Params<Category>,
        val productLists: AppDropdownMenuMapper.Params<ProductList>?,
        val atLeastOneProductJustAdded: Boolean,
        val title: CharSequence,
    )
}
