package app.grocery.list.custom.product.lists.picker.item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps

@Immutable
internal data class ProductListPickerItemProps(
    val id: String,
    val counter: Counter,
    val title: String,
    val stub: String?,
    val selected: Boolean,
    val deletable: Boolean,
    val demoColors: AppColorSchemeProps,
    val payload: Any? = null,
) {
    @Immutable
    sealed class Counter {

        abstract val alpha: Float
        abstract val totalSize: Int

        @Immutable
        data object NoItems : Counter() {
            override val totalSize = 0
            override fun toString() = "(0)"
            override val alpha = 0.42f
        }

        @Immutable
        data class JustTotalSize(
            override val totalSize: Int,
        ) : Counter() {
            override val alpha = 1f
            override fun toString() = "(${totalSize})"
        }

        @Immutable
        data class Ratio(
            val numberOfEnabled: Int,
            override val totalSize: Int,
        ) : Counter() {
            override val alpha = 1f
            override fun toString() = "($numberOfEnabled/$totalSize)"
        }
    }

}

internal class ProductListPickerItemMocks : PreviewParameterProvider<ProductListPickerItemProps> {

    override val values = sequenceOf(
        ProductListPickerItemProps(
            id = "1",
            counter = ProductListPickerItemProps.Counter.JustTotalSize(
                totalSize = 16,
            ),
            title = "My Lovely Supermarket",
            stub = "🍅 Tomatoes 2kg, 🍌 Bananas, 🥒 Cucumber, …",
            demoColors = AppColorSchemeProps.Yellow,
            deletable = true,
            selected = false,
        ),
        ProductListPickerItemProps(
            id = "2",
            counter = ProductListPickerItemProps.Counter.Ratio(
                numberOfEnabled = 3,
                totalSize = 4,
            ),
            title = "Electronic Store",
            stub = "Mouse, USB cable, Vacuum cleaner bags, …",
            demoColors = AppColorSchemeProps.Blue,
            deletable = true,
            selected = false,
        ),
        ProductListPickerItemProps(
            id = "3",
            counter = ProductListPickerItemProps.Counter.JustTotalSize(
                totalSize = 4,
            ),
            title = "Sport",
            stub = "Boots, T-Shirt, Sneakers, Helmet, …",
            demoColors = AppColorSchemeProps.Green,
            deletable = true,
            selected = true,
        ),
    )
}
