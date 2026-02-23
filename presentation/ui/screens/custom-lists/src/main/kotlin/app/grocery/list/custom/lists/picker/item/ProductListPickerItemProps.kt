package app.grocery.list.custom.lists.picker.item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.theme.BlueColor40
import app.grocery.list.commons.compose.theme.BlueColor60
import app.grocery.list.commons.compose.theme.BrandColor40
import app.grocery.list.commons.compose.theme.BrandColor60
import app.grocery.list.commons.compose.theme.GreenColor40
import app.grocery.list.commons.compose.theme.GreenColor60
import app.grocery.list.commons.compose.theme.MagentaColor40
import app.grocery.list.commons.compose.theme.MagentaColor60

@Immutable
internal data class ProductListPickerItemProps(
    val id: String,
    val title: String,
    val stub: String?,
    val selected: Boolean,
    val protected: Boolean,
    val demoColors: DemoColors,
    val hasDivider: Boolean,
) {
    @Immutable
    enum class DemoColors(
        val first: Color,
        val second: Color,
    ) {
        Yellow(
            first = BrandColor40,
            second = BrandColor60,
        ),
        Blue(
            first = BlueColor40,
            second = BlueColor60,
        ),
        Green(
            first = GreenColor40,
            second = GreenColor60,
        ),
        Magenta(
            first = MagentaColor40,
            second = MagentaColor60,
        ),
    }
}

internal class ProductListPickerItemMocks : PreviewParameterProvider<ProductListPickerItemProps> {

    override val values = sequenceOf(
        ProductListPickerItemProps(
            id = "1",
            title = "My Lovely Supermarket (16)",
            stub = "🍅 Tomatoes 2kg, 🍌 Bananas, 🥒 Cucumber, …",
            demoColors = ProductListPickerItemProps.DemoColors.Yellow,
            protected = false,
            selected = false,
            hasDivider = true,
        ),
        ProductListPickerItemProps(
            id = "2",
            title = "Electronic Store (3)",
            stub = "Mouse, USB cable, Vacuum cleaner bags, …",
            demoColors = ProductListPickerItemProps.DemoColors.Blue,
            protected = true,
            selected = false,
            hasDivider = true,
        ),
        ProductListPickerItemProps(
            id = "3",
            title = "Sport (4)",
            stub = "Boots, T-Shirt, Sneakers, Helmet, …",
            demoColors = ProductListPickerItemProps.DemoColors.Green,
            protected = true,
            selected = true,
            hasDivider = true,
        ),
    )
}
