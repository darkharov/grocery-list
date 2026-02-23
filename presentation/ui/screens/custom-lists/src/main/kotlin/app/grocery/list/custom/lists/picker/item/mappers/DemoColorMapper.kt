package app.grocery.list.custom.lists.picker.item.mappers

import app.grocery.list.custom.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.domain.product.list.ProductList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DemoColorMapper @Inject constructor() {

    fun toPresentation(colorScheme: ProductList.ColorScheme) =
        when (colorScheme) {
            ProductList.ColorScheme.Yellow -> {
                ProductListPickerItemProps.DemoColors.Yellow
            }
            ProductList.ColorScheme.Blue -> {
                ProductListPickerItemProps.DemoColors.Blue
            }
            ProductList.ColorScheme.Green -> {
                ProductListPickerItemProps.DemoColors.Green
            }
            ProductList.ColorScheme.Magenta -> {
                ProductListPickerItemProps.DemoColors.Magenta
            }
        }
}
