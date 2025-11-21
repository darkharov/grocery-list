package app.grocery.list.main.activity.ui.content

import androidx.compose.runtime.Immutable
import app.grocery.list.domain.product.Product

@Immutable
sealed class AppSnackbar {

    @Immutable
    data class UndoDeletionProduct(
        val product: Product,
        val formattedTitle: String,
    ) : AppSnackbar()
}
