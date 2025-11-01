package app.grocery.list.product.list.actions.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListMock
import app.grocery.list.domain.product.Product

@Immutable
internal sealed class ProductListActionsDialogProps {

    data object ConfirmClearList : ProductListActionsDialogProps()
    data object CopiedProductListNotFound : ProductListActionsDialogProps()

    @Immutable
    data class ConfirmPastedListWrapper(
        val confirmPastedList: ConfirmPastedListDialogProps,
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

    @Immutable
    data class ConfirmSharing(
        val numberOfProducts: Int,
        val recommendUsingThisApp: Boolean,
        val payload: List<Product>,
    ) : ProductListActionsDialogProps()
}

internal class ProductListActionsDialogMocks :
    CollectionPreviewParameterProvider<ProductListActionsDialogProps?>(
        listOf(
            ProductListActionsDialogProps.ConfirmClearList,
            ProductListActionsDialogProps.ConfirmPastedListWrapper(
                ConfirmPastedListMock.prototype
            ),
            ProductListActionsDialogProps.CopiedProductListNotFound,
            ProductListActionsDialogProps.HowToPutPastedProducts(productList = listOf()),
            ProductListActionsDialogProps.SublistToSharePicker(
                productListSize = 11,
                enabledItemsCount = 8,
                disabledItemsCount = 3,
            ),
            ProductListActionsDialogProps.ConfirmSharing(
                numberOfProducts = 4,
                recommendUsingThisApp = true,
                payload = emptyList(),
            ),
        )
    )
