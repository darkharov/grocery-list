package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuProps
import app.grocery.list.commons.compose.values.StringValue
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class ProductInputFormProps(
    val productId: Int?,
    val emoji: EmojiProps? = null,
    val enabled: Boolean = true,
    val categoriesDropdown: AppDropdownMenuProps = AppDropdownMenuProps(
        label = StringValue.ResId(R.string.category),
        items = persistentListOf(),
        expanded = false,
        selectedOne = null,
    ),
    val productListsDropdown: AppDropdownMenuProps? = null,
    val atLeastOneProductJustAdded: Boolean = false,
    val addButtonState: AppButtonStateProps = AppButtonStateProps.Gone,
    val doneButtonState: AppButtonStateProps = AppButtonStateProps.Gone,
)
