package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import app.grocery.list.domain.Product

@Immutable
internal sealed class ProductListActionsDialog {

    data object ConfirmClearList : ProductListActionsDialog()
    data object CopiedProductListNotFound : ProductListActionsDialog()

    @Immutable
    data class SublistToSharePicker(
        val productListSize: Int,
        val enabledItemsCount: Int,
        val disabledItemsCount: Int,
        val payload: Any? = null,
    ) : ProductListActionsDialog()

    data class ProductSuccessfullyAdded(val count: Int) : ProductListActionsDialog()

    data class HowToPutPastedProducts(
        val productList: List<Product>,
    ) : ProductListActionsDialog()
}

internal class ProductListActionsDialogMocks :
    CollectionPreviewParameterProvider<ProductListActionsDialog?>(
        listOf(
            ProductListActionsDialog.ConfirmClearList,
            ProductListActionsDialog.CopiedProductListNotFound,
            ProductListActionsDialog.HowToPutPastedProducts(productList = listOf()),
            ProductListActionsDialog.SublistToSharePicker(
                productListSize = 11,
                enabledItemsCount = 8,
                disabledItemsCount = 3,
            ),
            null,
        )
    )
