package app.grocery.list.product.input.form

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
import app.grocery.list.product.input.form.screen.ProductInputFormScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.launch

@Singleton
@AndroidEntryPoint
class ProductInputFormActivity :
    ComponentActivity(),
    ApplicationActivityMarker {

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var navigation: ProductInputFormNavigation

    private val viewModel: ProductInputFormViewModel by viewModels()

    override val isSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()
        handleEvents()
    }

    private fun setupContent() {
        setContent {
            themeUtil.GroceryListTheme {
                ProductInputFormScreen(
                    props = viewModel.props().collectAsState().value,
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

    private fun handle(event: ProductInputFormViewModel.Event) {
        when (event) {
            ProductInputFormViewModel.Event.OnGoToPreview -> {
                navigation.goToProductListPreview()
            }
        }
    }
}
