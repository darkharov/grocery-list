package app.grocery.list.custom.product.lists.picker.item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps
import app.grocery.list.commons.compose.props.AppCounterProps

@Immutable
internal data class ProductListPickerItemProps(
    val id: String,
    val counter: AppCounterProps,
    val title: String,
    val stub: String?,
    val selected: Boolean,
    val deletable: Boolean,
    val colorScheme: AppColorSchemeProps,
    val payload: Any? = null,
)

internal class ProductListPickerItemMocks : PreviewParameterProvider<ProductListPickerItemProps> {

    override val values = sequenceOf(
        ProductListPickerItemProps(
            id = "1",
            counter = AppCounterProps.JustTotalSize(
                totalSize = 16,
            ),
            title = "My Lovely Supermarket",
            stub = "🍅 Tomatoes 2kg, 🍌 Bananas, 🥒 Cucumber, …",
            colorScheme = AppColorSchemeProps.Yellow,
            deletable = true,
            selected = false,
        ),
        ProductListPickerItemProps(
            id = "2",
            counter = AppCounterProps.Ratio(
                numberOfEnabled = 3,
                totalSize = 4,
            ),
            title = "Electronic Store",
            stub = "Mouse, USB cable, Vacuum cleaner bags, …",
            colorScheme = AppColorSchemeProps.Blue,
            deletable = true,
            selected = false,
        ),
        ProductListPickerItemProps(
            id = "3",
            counter = AppCounterProps.JustTotalSize(
                totalSize = 4,
            ),
            title = "Sport",
            stub = "Boots, T-Shirt, Sneakers, Helmet, …",
            colorScheme = AppColorSchemeProps.Green,
            deletable = true,
            selected = true,
        ),
    )
}
