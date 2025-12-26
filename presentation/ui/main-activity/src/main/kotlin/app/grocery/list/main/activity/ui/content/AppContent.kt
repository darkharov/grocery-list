package app.grocery.list.main.activity.ui.content

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminder
import app.grocery.list.clear.notifications.reminder.clearNotificationsReminder
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.toolbar.AppToolbar
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.final_.steps.FinalSteps
import app.grocery.list.final_.steps.finalSteps
import app.grocery.list.main.activity.R
import app.grocery.list.product.input.form.productInputForm
import app.grocery.list.product.list.actions.bar.ProductListActionsBar
import app.grocery.list.product.list.actions.screen.productListActions
import app.grocery.list.product.list.preview.ProductListPreview
import app.grocery.list.product.list.preview.productListPreview
import app.grocery.list.settings.Settings
import app.grocery.list.settings.settingsAndChildScreens
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitch
import app.grocery.list.settings.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@Composable
internal fun AppContent(
    numberOfEnabledProducts: Int?,
    progress: Boolean,
    hasEmojiIfEnoughSpace: Boolean,
    dialog: AppLevelDialog?,
    delegate: AppContentDelegate,
    appEvents: ReceiveChannel<AppEvent>,
    snackbars: ReceiveChannel<AppSnackbar>,
    modifier: Modifier = Modifier,
) {
    val startRoute = ProductListPreview
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { navBackStackEntry ->
            val destination = navBackStackEntry.destination
            delegate.handleCurrentDestinationChange(destination)
        }
    }

    EventConsumer(appEvents) { event ->
        when (event) {
            is AppEvent.PushNotificationsGranted -> {
                val reminderEnabled = event.clearNotificationsReminderEnabled
                val route: Any = if (reminderEnabled) {
                    ClearNotificationsReminder
                } else {
                    FinalSteps
                }
                navController.navigate(route)
            }
        }
    }

    val undo = stringResource(R.string.undo)
    val productWasDeletedPattern = stringResource(R.string.pattern_product_was_deleted)

    LaunchedEffect(Unit) {
        for (snackbar in snackbars) {
            when (snackbar) {
                is AppSnackbar.UndoDeletionProduct -> {
                    val result = snackbarHostState.showSnackbar(
                        message = productWasDeletedPattern.format(snackbar.formattedTitle),
                        actionLabel = undo.uppercase(),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        delegate.undoProductDeletion(snackbar.product)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            AppToolbar(
                props = AppToolbarProps(
                    content = AppToolbarContentCollection
                        .Instance
                        .getOrDefault(currentDestination?.destination) {
                            AppToolbarProps.Content.Default(
                                counter = numberOfEnabledProducts,
                                onStart = currentDestination
                                    ?.destination
                                    ?.hasRoute(startRoute::class) == true,
                                hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                            )
                        },
                    progress = progress,
                ),
                onUpClick = {
                    navController.popBackStack()
                },
                onTrailingIconClick = {
                    navController.navigate(Settings)
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier
                            .padding(bottom = 120.dp),
                    )
                }
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
                        .union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Horizontal),
                )
                .padding(padding),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            sizeTransform = null,
        ) {
            val navigation = AppNavigationFacade(navController)
            productListPreview(
                delegate = delegate,
                navigation = navigation,
                bottomBar = {
                    ProductListActionsBar(
                        navigation = navigation,
                        delegate = delegate,
                    )
                },
            )
            productInputForm(navigation)
            productListActions(
                delegate = delegate,
                navigation = navigation,
                bottomElement = {
                    UseIconsOnBottomBarSwitch(
                        strategy = UseIconsOnBottomBarSwitchStrategy.EmbeddedElement,
                        navigation = navigation,
                    )
                },
            )
            clearNotificationsReminder(navigation)
            finalSteps(navigation)
            settingsAndChildScreens(delegate, navController)
        }
    }

    if (dialog != null) {
        when (dialog) {
            AppLevelDialog.AppPushNotificationsDenied -> {
                AppSimpleDialog(
                    icon = rememberVectorPainter(AppIcons.notifications),
                    text = StringValue.ResId(R.string.notification_permission_explanation),
                    onDismiss = {
                        delegate.handleDialogDismiss()
                    },
                    onCancel = {
                        delegate.handleDialogDismiss()
                    },
                    confirmButtonText = StringValue.ResId(R.string.give_permission),
                    onConfirm = {
                        delegate.handleDialogDismiss()
                        delegate.openNotificationSettings()
                    },
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun AppContentPreview() {
    GroceryListTheme {
        AppContent(
            numberOfEnabledProducts = 42,
            progress = false,
            hasEmojiIfEnoughSpace = true,
            dialog = null,
            delegate = AppContentDelegateMock,
            appEvents = Channel(),
            snackbars = Channel(),
        )
    }
}
