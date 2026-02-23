package app.grocery.list.product.input.form.mappers

import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuItemMapper
import app.grocery.list.domain.category.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CategoryDropdownMenuItemMapper @Inject constructor() : AppDropdownMenuItemMapper<Category>() {

    override fun id(item: Category): String =
        item.id.toString()

    override fun title(item: Category): String =
        item.title
}
