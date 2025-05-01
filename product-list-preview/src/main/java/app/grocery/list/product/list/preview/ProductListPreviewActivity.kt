package app.grocery.list.product.list.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.grocery.list.commons.app.ApplicationActivityMarker
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.product.list.preview.screen.ProductListPreviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListPreviewActivity :
    ComponentActivity(),
    ApplicationActivityMarker {

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var navigation: ProductListPreviewNavigation

    private val viewModel by viewModels<ProductListPreviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()
        handleEvents()
    }

    private fun setupContent() {
        setContent {
            themeUtil.GroceryListTheme {
                ProductListPreviewScreen(
                    props = viewModel.props.collectAsState().value,
                    callbacks = viewModel,
                )
            }
        }
    }

    private fun handleEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                for (event in viewModel.events()) {
                    handle(event)
                }
            }
        }
    }

    private fun handle(event: ProductListPreviewViewModel.Event) {
        when (event) {
            ProductListPreviewViewModel.Event.OnNexOptionSelected -> {
                navigation.goToProductListActions()
            }
        }
    }
}
