package app.grocery.list.main.activity.ui.content

import androidx.compose.animation.ContentTransform
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderScreen
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.toolbar.AppToolbar
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.final_.steps.FinalStepsScreen
import app.grocery.list.main.activity.R
import app.grocery.list.product.input.form.ProductInputFormScreen
import app.grocery.list.product.list.actions.ProductListActionsScreen
import app.grocery.list.product.list.actions.bar.ProductListActionsBar
import app.grocery.list.product.list.preview.ProductListPreviewScreen
import app.grocery.list.settings.SettingsScreen
import app.grocery.list.settings.bottom.bar.settings.BottomBarSettingsScreen
import app.grocery.list.settings.list.format.ListFormatSettingsScreen
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
    val backStack = rememberNavBackStack(ProductListPreview)
    val snackbarHostState = remember { SnackbarHostState() }
    val navigation = remember { AppNavigationFacade(backStack) }

    EventConsumer(
        events = appEvents,
        lifecycleState = Lifecycle.State.CREATED,
    ) { event ->
        when (event) {
            is AppEvent.PushNotificationsGranted -> {
                val reminderEnabled = event.clearNotificationsReminderEnabled
                val key = if (reminderEnabled) {
                    ClearNotificationsReminder
                } else {
                    FinalSteps
                }
                backStack.add(key)
            }
            is AppEvent.ScreenLocked -> {
                if (backStack.last() is FinalSteps) {
                    delegate.notificationPublisher.tryToPost()
                }
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
                    content = ToolbarContentUtil
                        .customContentOrNull(navKey = backStack.last())
                        ?: AppToolbarProps.Content.Default(
                            counter = numberOfEnabledProducts,
                            onStart = backStack.size == 1,
                            hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                        ),
                    progress = progress,
                ),
                onUpClick = {
                    backStack.removeLastOrNull()
                },
                onTrailingIconClick = {
                    backStack.add(Settings)
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
        NavDisplay(
            backStack = backStack,
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets
                        .navigationBars
                        .union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Horizontal),
                )
                .padding(padding),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            sizeTransform = null,
            transitionSpec = {
                ContentTransform(
                    EnterTransition.None,
                    ExitTransition.None,
                )
            },
            popTransitionSpec = {
                ContentTransform(
                    EnterTransition.None,
                    ExitTransition.None,
                )
            },
            predictivePopTransitionSpec = {
                ContentTransform(
                    EnterTransition.None,
                    ExitTransition.None,
                )
            },
            entryProvider = entryProvider {
                entry<ClearNotificationsReminder> {
                    ClearNotificationsReminderScreen(
                        navigation = navigation,
                    )
                }
                entry<FinalSteps> {
                    FinalStepsScreen(
                        navigation = navigation,
                    )
                }
                entry<ProductInputForm> { key ->
                    ProductInputFormScreen(
                        productId = key.productId,
                        navigation = navigation,
                    )
                }
                entry<ProductListActions> {
                    ProductListActionsScreen(
                        delegate = delegate,
                        navigation = navigation,
                        bottomElement = {
                            UseIconsOnBottomBarSwitch(
                                strategy = UseIconsOnBottomBarSwitchStrategy.EmbeddedElement,
                                navigation = navigation,
                            )
                        },
                    )
                }
                entry<ProductListPreview> {
                    ProductListPreviewScreen(
                        navigation = navigation,
                        delegate = delegate,
                        bottomBar = {
                            ProductListActionsBar(
                                navigation = navigation,
                                delegate = delegate,
                            )
                        },
                    )
                }
                entry<Settings> {
                    SettingsScreen(
                        delegate = delegate,
                        navigation = navigation,
                    )
                }
                entry<ListFormatSettings> {
                    ListFormatSettingsScreen()
                }
                entry<BottomBarSettings> {
                    BottomBarSettingsScreen(
                        navigation = navigation,
                    )
                }
            },
        )
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
