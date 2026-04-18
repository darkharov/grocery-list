package app.grocery.list.product.list.preview.elements.product.item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
data class ProductItemProps(
    val key: String,
    val title: AnnotatedString,
    val enabled: Boolean = true,
    val payload: Any? = null,
)

internal class ProductItemMocks : PreviewParameterProvider<ProductItemProps> {

    override val values = sequenceOf(
        ProductItemProps(
            key = "1",
            enabled = true,
            title = AnnotatedString("🍅 Tomato"),
        ),
    )
}
