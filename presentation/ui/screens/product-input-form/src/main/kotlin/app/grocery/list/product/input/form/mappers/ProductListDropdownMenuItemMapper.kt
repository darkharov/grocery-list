package app.grocery.list.product.input.form.mappers

import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuItemMapper
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductListDropdownMenuItemMapper @Inject constructor() : AppDropdownMenuItemMapper<ProductList>() {

    override fun id(item: ProductList): String =
        item.id.toString()

    override fun title(item: ProductList): String =
        item.title
}
