package app.grocery.list.product.list.preview.elements.neighbours

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.props.AppCounterProps

@Immutable
data class ProductListNeighboursProps(
    val key: String,
    val trailing: Item?,
    val leading: Item?,
) {
    @Immutable
    data class Item(
        val title: String,
        val counter: AppCounterProps,
        val payload: Any? = null,
    )
}

internal class ProductListNeighboursMocks : PreviewParameterProvider<ProductListNeighboursProps> {

    override val values = sequenceOf(
        prototype,
        prototype.copy(
            trailing = trailingItem
                .copy(
                    counter = AppCounterProps.Ratio(
                        numberOfEnabled = 4,
                        totalSize = 5,
                    )
                ),
        ),
    )

    companion object {
        val trailingItem = ProductListNeighboursProps.Item(
            title = "Trailing",
            counter = AppCounterProps.JustTotalSize(
                totalSize = 11,
            ),
        )
        val prototype = ProductListNeighboursProps(
            key = "key",
            trailing = trailingItem,
            leading = ProductListNeighboursProps.Item(
                title = "Leading",
                counter = AppCounterProps.NoItems,
            )
        )
    }
}
