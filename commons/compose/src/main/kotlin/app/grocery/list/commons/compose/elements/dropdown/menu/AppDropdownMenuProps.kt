package app.grocery.list.commons.compose.elements.dropdown.menu

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.values.StringValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AppDropdownMenuProps(
    val label: StringValue,
    val items: ImmutableList<Item>,
    val expanded: Boolean,
    val selectedOne: Item?,
) {
    @Immutable
    data class Item(
        val id: String,
        val title: String,
        val payload: Any? = null,
    )
}

class AppDropdownMenuMocks : PreviewParameterProvider<AppDropdownMenuProps> {

    companion object {

        private val itemsPrototype =
            persistentListOf(
                AppDropdownMenuProps.Item(
                    id = "1",
                    title = "First item",
                ),
                AppDropdownMenuProps.Item(
                    id = "2",
                    title = "Second item",
                ),
                AppDropdownMenuProps.Item(
                    id = "3",
                    title = "Third item",
                ),
            )

        val prototype =
            AppDropdownMenuProps(
                label = StringValue.StringWrapper("label"),
                items = itemsPrototype,
                expanded = true,
                selectedOne = itemsPrototype.first(),
            )
    }

    override val values =
        sequenceOf(
            prototype,
            prototype.copy(expanded = false),
            prototype.copy(selectedOne = null),
        )
}
