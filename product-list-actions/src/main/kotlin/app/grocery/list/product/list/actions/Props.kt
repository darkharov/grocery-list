package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable

@Immutable
data class ProductListActionsProps(
    val loadingListToShare: Boolean,
    val numberOfProducts: Int,
)
