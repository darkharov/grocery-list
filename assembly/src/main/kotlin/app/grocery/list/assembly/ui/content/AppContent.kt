package app.grocery.list.assembly.ui.content

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.grocery.list.assembly.R
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppToolbar
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.preparing.for_.shopping.PreparingForShopping
import app.grocery.list.preparing.for_.shopping.preparingForShopping
import app.grocery.list.product.input.form.ProductInputForm
import app.grocery.list.product.input.form.productInputFormScreen
import app.grocery.list.product.list.actions.productListActionsScreen
import app.grocery.list.product.list.preview.productListPreviewScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@Composable
internal fun AppContent(
    numberOfAddedProducts: Int,
    navController: NavHostController,
    delegates: AppContentDelegate,
    appEvents: ReceiveChannel<AppEvent>,
    modifier: Modifier = Modifier,
) {
    var upAvailable: Boolean by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            upAvailable = controller.currentDestination?.id != controller.graph.startDestinationId
            val route = destination.route
            if (route != null) {
                delegates.onScreenChange(route)
            }
        }
    }
    EventConsumer(
        key = appEvents,
        events = appEvents,
        lifecycleState = Lifecycle.State.RESUMED,
    ) { event ->
        when (event) {
            AppEvent.PushNotificationsGranted -> {
                navController.navigate(PreparingForShopping)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.grocery_list),
                counterValue = numberOfAddedProducts,
                onUpClick = if (upAvailable) {
                    { navController.popBackStack() }
                } else {
                    null
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = ProductInputForm,
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets
                        .navigationBars
                        .only(WindowInsetsSides.Horizontal),
                )
                .padding(padding)
        ) {
            val navigation = AppNavigationFacade(navController)
            productInputFormScreen(navigation)
            productListPreviewScreen(navigation)
            productListActionsScreen(navigation, delegates)
            preparingForShopping()
        }
    }
}

@Composable
@PreviewLightDark
private fun AppContentPreview() {
    GroceryListTheme {
        AppContent(
            numberOfAddedProducts = 42,
            delegates = AppContentDelegateMock,
            navController = rememberNavController(),
            appEvents = Channel(),
        )
    }
}
