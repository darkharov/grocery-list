package app.grocery.list.product.input.form

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object ProductInputForm

interface ProductInputFormNavigation {
    fun onGoToPreview()
}

fun NavGraphBuilder.productInputFormScreen(
    navigation: ProductInputFormNavigation,
) {
    composable<ProductInputForm> {
        ProductInputFormScreen(
            navigation = navigation,
        )
    }
}
