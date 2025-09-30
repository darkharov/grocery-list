package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps

@Immutable
internal class ProductListActionsProps(
    val useIconsOnBottomBar: Boolean,
    val loadingListToShare: Boolean,
    val numberOfProducts: Int,
    val atLeastOneProductEnabled: Boolean,
) {
    val listEmpty = numberOfProducts == 0
    val clearListAvailable = !(listEmpty)
    val shoppingAvailable = !(listEmpty)
    val shareButtonState = when {
        loadingListToShare -> AppButtonStateProps.Loading
        else -> AppButtonStateProps.enabled(!(listEmpty))
    }
}

internal class ProductListActionsMocks : PreviewParameterProvider<ProductListActionsProps> {

    override val values =
        sequenceOf(0, 11).map {
            ProductListActionsProps(
                loadingListToShare = false,
                numberOfProducts = 11,
                atLeastOneProductEnabled = true,
                useIconsOnBottomBar = false,
            )
        }
}
