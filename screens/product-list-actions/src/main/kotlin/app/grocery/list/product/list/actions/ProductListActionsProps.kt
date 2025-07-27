package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable

@Immutable
internal data class ProductListActionsProps(
    val loadingListToShare: Boolean,
    val numberOfProducts: Int,
)
