package app.grocery.list.product.list.actions

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.grocery.list.commons.app.ApplicationActivityMarker
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event.OnExitOptionSelected
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event.OnListCleared
import app.grocery.list.product.list.actions.ProductListActionsViewModel.Event.OnStartShoppingOptionSelected
import app.grocery.list.product.list.actions.screen.ProductListActionsScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListActionsActivity :
    ComponentActivity(),
    ApplicationActivityMarker {

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var navigation: ProductListActionsNavigation

    private val viewModel by viewModels<ProductListActionsViewModel>()

    private val postNotifications = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            postNotificationsAndExitFromApp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()
        handleEvents()
    }

    private fun setupContent() {
        setContent {
            themeUtil.GroceryListTheme {
                ProductListActionsScreen(
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

    private fun handle(event: ProductListActionsViewModel.Event) {
        when (event) {
            OnListCleared -> {
                navigation.backToProductInputForm()
            }
            OnExitOptionSelected -> {
                finishAffinity()
            }
            OnStartShoppingOptionSelected -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    postNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    postNotificationsAndExitFromApp()
                }
            }
        }
    }

    private fun postNotificationsAndExitFromApp() {
        viewModel.postNotifications()
        finishAffinity()
    }
}
