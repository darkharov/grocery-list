package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable

@Immutable
internal data class ProductListActionsProps(
    val productListCount: Int,
    val atLeastOneProductEnabled: Boolean,
    val loadingListToShare: Boolean,
)
