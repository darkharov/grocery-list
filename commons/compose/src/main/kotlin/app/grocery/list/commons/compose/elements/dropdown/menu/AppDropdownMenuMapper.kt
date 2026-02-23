package app.grocery.list.commons.compose.elements.dropdown.menu

import app.grocery.list.commons.compose.values.StringValue
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class AppDropdownMenuMapper<T : Any>(
    private val label: StringValue,
    private val itemMapper: AppDropdownMenuItemMapper<T>,
) {
    fun toPresentation(params: Params<T>) =
        AppDropdownMenuProps(
            label = label,
            items = params.items.map { itemMapper.toPresentation(it) }.toImmutableList(),
            expanded = params.expanded,
            selectedOne = if (params.selectedOne != null) {
                itemMapper.toPresentation(params.selectedOne)
            } else {
                null
            },
        )

    data class Params<T : Any>(
        val items: List<T> = persistentListOf(),
        val selectedOne: T? = null,
        val expanded: Boolean = false,
    )
}
