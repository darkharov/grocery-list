package app.grocery.list.commons.compose.elements.dropdown.menu

abstract class AppDropdownMenuItemMapper<T : Any> {

    protected abstract fun id(item: T): String
    protected abstract fun title(item: T): String

    fun toPresentation(item: T): AppDropdownMenuProps.Item =
        AppDropdownMenuProps.Item(
            id = id(item),
            title = title(item),
            payload = item,
        )

    fun toDomainNullable(item: AppDropdownMenuProps.Item?): T? =
        if (item != null) {
            toDomain(item)
        } else {
            null
        }

    @Suppress("UNCHECKED_CAST")
    fun toDomain(item: AppDropdownMenuProps.Item): T =
        item.payload as T
}
