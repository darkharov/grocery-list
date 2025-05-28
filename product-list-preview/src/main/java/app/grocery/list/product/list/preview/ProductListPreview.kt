package app.grocery.list.product.list.preview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.product.list.preview.screen.ProductListPreviewScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProductListPreview

interface ProductListPreviewNavigation {
    fun onGoToActions()
}

fun NavGraphBuilder.productListPreviewScreen(
    navigation: ProductListPreviewNavigation,
) {
    composable<ProductListPreview> {
        ProductListPreviewScreen(
            navigation = navigation,
        )
    }
}
