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
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderScreen
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.toolbar.AppToolbar
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.faq.FaqScreen
import app.grocery.list.final_.steps.FinalStepsScreen
import app.grocery.list.main.activity.R
import app.grocery.list.product.input.form.ProductInputFormScreen
import app.grocery.list.product.list.actions.ProductListActionsScreen
import app.grocery.list.product.list.actions.bar.ProductListActionsBar
import app.grocery.list.product.list.preview.ProductListPreviewScreen
import app.grocery.list.settings.SettingsScreen
import app.grocery.list.settings.child.screens.bottom.bar.settings.BottomBarSettingsScreen
import app.grocery.list.settings.child.screens.list.format.ListFormatSettingsScreen
import app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitch
import app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_.UseIconsOnBottomBarSwitchStrategy

@Composable
internal fun AppContent(
    backStack: List<NavKey>,
    numberOfEnabledProducts: Int?,
    progress: Boolean,
    hasEmojiIfEnoughSpace: Boolean,
    dialog: AppLevelDialog?,
    contract: AppContentContract,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val undo = stringResource(R.string.undo)
    val productWasDeletedPattern = stringResource(R.string.pattern_product_was_deleted)

    LaunchedEffect(Unit) {
        for (snackbar in contract.snackbars()) {
            when (snackbar) {
                is AppSnackbar.UndoDeletionProduct -> {
                    val result = snackbarHostState.showSnackbar(
                        message = productWasDeletedPattern.format(snackbar.formattedTitle),
                        actionLabel = undo.uppercase(),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        contract.undoProductDeletion(snackbar.product)
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = LocalAppColors.current.background,
        contentColor = LocalAppColors.current.blackOrWhite,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            AppToolbar(
                props = AppToolbarProps(
                    content = ToolbarContentUtil
                        .customContentOrNull(navKey = backStack.last())
                        ?: AppToolbarProps.Content.Default(
                            counter = numberOfEnabledProducts,
                            isOnStart = backStack.size == 1,
                            hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                        ),
                    progress = progress,
                ),
                callbacks = contract,
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
                        contract = contract,
                    )
                }
                entry<FinalSteps> {
                    FinalStepsScreen(
                        contract = contract,
                    )
                }
                entry<ProductInputForm> { key ->
                    ProductInputFormScreen(
                        productId = key.productId,
                        contract = contract,
                    )
                }
                entry<ProductListActions> {
                    ProductListActionsScreen(
                        contract = contract,
                        bottomElement = {
                            UseIconsOnBottomBarSwitch(
                                strategy = UseIconsOnBottomBarSwitchStrategy.EmbeddedElement,
                                contract = contract,
                            )
                        },
                    )
                }
                entry<ProductListPreview> {
                    ProductListPreviewScreen(
                        contract = contract,
                        bottomBar = {
                            ProductListActionsBar(
                                contract = contract,
                            )
                        },
                    )
                }
                entry<Settings> {
                    SettingsScreen(
                        contract = contract,
                    )
                }
                entry<ListFormatSettings> {
                    ListFormatSettingsScreen()
                }
                entry<BottomBarSettings> {
                    BottomBarSettingsScreen(
                        contract = contract,
                    )
                }
                entry<Faq> {
                    FaqScreen()
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
                        contract.dismissDialog()
                    },
                    onCancel = {
                        contract.dismissDialog()
                    },
                    confirmButtonText = StringValue.ResId(R.string.give_permission),
                    onConfirm = {
                        contract.dismissDialog()
                        contract.openNotificationSettings()
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
            contract = AppContentContractMock,
            backStack = remember { mutableListOf(ProductListPreview) },
        )
    }
}
