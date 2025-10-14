package app.grocery.list.product.list.actions.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import app.grocery.list.domain.Product

@Immutable
internal sealed class ProductListActionsDialogProps {

    data object ConfirmClearList : ProductListActionsDialogProps()
    data object CopiedProductListNotFound : ProductListActionsDialogProps()

    @Immutable
    data class ConfirmPastedList(
        val numberOfFoundProducts: Int,
        val titlesAsString: String,
        val productList: List<Product>,
    ) : ProductListActionsDialogProps()

    @Immutable
    data class SublistToSharePicker(
        val productListSize: Int,
        val enabledItemsCount: Int,
        val disabledItemsCount: Int,
        val payload: Any? = null,
    ) : ProductListActionsDialogProps()

    data class ProductSuccessfullyAdded(val count: Int) : ProductListActionsDialogProps()

    data class HowToPutPastedProducts(
        val productList: List<Product>,
    ) : ProductListActionsDialogProps()

    @Immutable
    data class EnableAllAndStartShopping(
        val totalProductCount: Int,
    ) : ProductListActionsDialogProps()
}

internal class ProductListActionsDialogMocks :
    CollectionPreviewParameterProvider<ProductListActionsDialogProps?>(
        listOf(
            ProductListActionsDialogProps.ConfirmClearList,
            ProductListActionsDialogProps.ConfirmPastedList(
                numberOfFoundProducts = 2,
                titlesAsString = "Broccoli,\nCoffee",
                productList = emptyList(),
            ),
            ProductListActionsDialogProps.CopiedProductListNotFound,
            ProductListActionsDialogProps.HowToPutPastedProducts(productList = listOf()),
            ProductListActionsDialogProps.SublistToSharePicker(
                productListSize = 11,
                enabledItemsCount = 8,
                disabledItemsCount = 3,
            ),
        )
    )
