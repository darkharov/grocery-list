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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import app.darkharov.clear.notifications.reminder.ClearNotificationsReminder
import app.darkharov.clear.notifications.reminder.clearNotificationsReminder
import app.grocery.list.assembly.R
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppToolbar
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.preparing.for_.shopping.PreparingForShopping
import app.grocery.list.preparing.for_.shopping.preparingForShopping
import app.grocery.list.product.input.form.productInputFormScreen
import app.grocery.list.product.list.actions.productListActionsScreen
import app.grocery.list.product.list.preview.ProductListPreview
import app.grocery.list.product.list.preview.productListPreviewScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@Composable
internal fun AppContent(
    numberOfAddedProducts: Int?,
    progress: Boolean,
    delegates: AppContentDelegate,
    appEvents: ReceiveChannel<AppEvent>,
    modifier: Modifier = Modifier,
) {
    val startRoute = ProductListPreview
    val navController = rememberNavController()
    var currentDestination by remember { mutableStateOf<NavDestination?>(null) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { navBackStackEntry ->
            val destination = navBackStackEntry.destination
            currentDestination = destination
            delegates.onCurrentDestinationChange(destination)
        }
    }

    EventConsumer(
        key = appEvents,
        events = appEvents,
        lifecycleState = Lifecycle.State.RESUMED,
    ) { event ->
        when (event) {
            is AppEvent.PushNotificationsGranted -> {
                val reminderEnabled = event.clearNotificationsReminderEnabled
                val route: Any = if (reminderEnabled) {
                    ClearNotificationsReminder
                } else {
                    PreparingForShopping
                }
                navController.navigate(route)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            AppToolbar(
                title = stringResource(R.string.grocery_list),
                progress = progress,
                counterValue = numberOfAddedProducts,
                onUpClick = if (
                    currentDestination?.hasRoute(startRoute::class) == true
                ) {
                    null
                } else {
                    { navController.popBackStack() }
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets
                        .navigationBars
                        .only(WindowInsetsSides.Horizontal),
                )
                .padding(padding)
        ) {
            val navigation = AppNavigationFacade(
                startRoute = startRoute,
                navController = navController,
            )
            productListPreviewScreen(navigation)
            productInputFormScreen(navigation)
            productListActionsScreen(navigation, delegates)
            clearNotificationsReminder(navigation)
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
            appEvents = Channel(),
            progress = false,
        )
    }
}
