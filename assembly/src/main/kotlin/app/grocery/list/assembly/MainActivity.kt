package app.grocery.list.assembly

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.grocery.list.commons.app.ApplicationActivityMarker
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.commons.compose.theme.elements.AppCounter
import app.grocery.list.commons.compose.theme.elements.toolbar.AppToolbar
import app.grocery.list.notifications.NotificationPublisher
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.ProductInputFormNavigation
import app.grocery.list.product.input.form.productInputFormScreen
import app.grocery.list.product.list.actions.ProductListActions
import app.grocery.list.product.list.actions.ProductListActionsNavigation
import app.grocery.list.product.list.actions.productListActionsScreen
import app.grocery.list.product.list.preview.ProductListPreview
import app.grocery.list.product.list.preview.ProductListPreviewNavigation
import app.grocery.list.product.list.preview.productListPreviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    ApplicationActivityMarker {

    override val isSplashScreen = true

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher

    private val viewModel by viewModels<MainViewModel>()

    private val postNotifications = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) {
            postNotificationsAndExitFromApp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val numberOfAddedProduct by viewModel.numberOfAddedProduct.collectAsState()
            val navController = rememberNavController()
            val navigationFacade = remember { NavigationFacade(navController) }
            themeUtil.GroceryListTheme {
                Content(
                    numberOfAddedProduct = numberOfAddedProduct,
                    navigationFacade = navigationFacade,
                    navController = navController,
                )
            }
        }
    }

    private fun postNotificationsAndExitFromApp() {
        lifecycleScope.launch {
            val productList = viewModel.productList.filterNotNull().first()
            notificationPublisher.tryToPost(productList)
            finish()
        }
    }

    inner class NavigationFacade(
        private val navController: NavController,
    ) : ProductInputFormNavigation,
        ProductListPreviewNavigation,
        ProductListActionsNavigation {

        override fun onGoToPreview() {
            navController.navigate(ProductListPreview)
        }

        override fun onGoToActions() {
            navController.navigate(ProductListActions)
        }

        override fun onListCleared() {
            navController.popBackStack(ProductInputForm, false)
        }

        override fun onExitFromApp() {
            finish()
        }

        override fun onStartShopping() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                postNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                postNotificationsAndExitFromApp()
            }
        }
    }
}

@Composable
private fun Content(
    numberOfAddedProduct: Int,
    navController: NavHostController,
    navigationFacade: MainActivity.NavigationFacade,
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.grocery_list),
                titleTrailingContent = {
                    AppCounter(
                        value = numberOfAddedProduct,
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                    )
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ProductInputForm,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            builder = {
                productInputFormScreen(navigationFacade)
                productListPreviewScreen(navigationFacade)
                productListActionsScreen(navigationFacade)
            }
        )
    }
}
