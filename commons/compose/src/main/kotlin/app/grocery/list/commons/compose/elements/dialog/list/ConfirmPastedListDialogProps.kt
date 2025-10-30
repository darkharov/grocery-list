package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.runtime.Immutable
import app.grocery.list.domain.Product

@Immutable
data class ConfirmPastedListDialogProps(
    val numberOfFoundProducts: Int,
    val titlesAsString: String,
    val productList: List<Product>,
)

object ConfirmPastedListMock {
    val prototype = ConfirmPastedListDialogProps(
        numberOfFoundProducts = 2,
        titlesAsString = "Broccoli,\nCoffee",
        productList = emptyList(),
    )
}
