package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.product.Product

@Immutable
data class ConfirmPastedListDialogProps(
    val title: StringValue,
    val text: String,
    val productList: List<Product>,
)

object ConfirmPastedListMock {
    val prototype = ConfirmPastedListDialogProps(
        title = StringValue.StringWrapper("Title"),
        text = "Broccoli,\nCoffee",
        productList = emptyList(),
    )
}
