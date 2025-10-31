package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.Product

@Immutable
data class ConfirmPastedListDialogProps(
    val title: StringValue,
    val text: String,
    val productList: List<Product>,
)

object ConfirmPastedListMock {
    val prototype = ConfirmPastedListDialogProps(
        title = StringValue.StringWrapper(""),
        text = "Broccoli,\nCoffee",
        productList = emptyList(),
    )
}
