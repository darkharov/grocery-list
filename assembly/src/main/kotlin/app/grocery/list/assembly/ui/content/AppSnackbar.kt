package app.grocery.list.assembly.ui.content

import androidx.compose.runtime.Immutable
import app.grocery.list.domain.Product

@Immutable
sealed class AppSnackbar {

    @Immutable
    data class UndoDeletionProduct(
        val product: Product,
        val formattedTitle: String,
    ) : AppSnackbar()
}
