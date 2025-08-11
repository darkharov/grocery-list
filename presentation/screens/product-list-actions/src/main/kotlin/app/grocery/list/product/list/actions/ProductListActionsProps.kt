package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable

@Immutable
internal data class ProductListActionsProps(
    val productListEmpty: Boolean,
    val loadingListToShare: Boolean,
)
