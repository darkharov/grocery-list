package app.grocery.list.product.list.actions

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider

@Immutable
internal enum class ProductListActionsDialog {
    ConfirmClearList,
}

internal class ProductListActionsDialogMocks :
    CollectionPreviewParameterProvider<ProductListActionsDialog?>(
        ProductListActionsDialog.entries.toList() + null
    )
