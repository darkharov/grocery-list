package app.grocery.list.main.activity.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.main.activity.ui.MainViewModel.Event
import app.grocery.list.main.activity.ui.content.AppContent
import app.grocery.list.main.activity.ui.content.FinalSteps
import app.grocery.list.notifications.NotificationPublisher
import commons.android.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    PermissionUtil.Contract {

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher

    private val viewModel by viewModels<MainViewModel>()
    private val permissionUtil = PermissionUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setupContent()
    }

    private fun setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }

    private fun setupContent() {
        setContent {
            val numberOfEnabledProducts by viewModel.numberOfEnabledProducts.collectAsState()
            val progress by viewModel.progress.collectAsState()
            val hasEmojiIfEnoughSpace by viewModel.hasEmojiIfEnoughSpace.collectAsState()
            val dialog by viewModel.dialog().collectAsStateWithLifecycle()
            themeUtil.GroceryListTheme {
                AppContent(
                    backStack = viewModel.backStack,
                    numberOfEnabledProducts = numberOfEnabledProducts,
                    progress = progress,
                    hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                    dialog = dialog,
                    contract = viewModel,
                )
                LaunchedEffect(Unit) {
                    snapshotFlow {
                        viewModel.backStack.last() is FinalSteps
                    }.collectLatest {
                        notificationPublisher.notifyIsUserOnFinalScreenChange(it)
                    }
                }
                EventConsumer(viewModel.events()) { event ->
                    when (event) {
                        is Event.OnExitFromAppSelected -> {
                            finish()
                        }
                        is Event.OnNotificationSettingsRequired -> {
                            openNotificationSettings()
                        }
                        is Event.OnStartShopping -> {
                            permissionUtil.requestPostNotifications()
                        }
                    }
                }
            }
        }
    }

    private fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }

    override fun onPostNotificationsGranted() {
        viewModel.notifyPushNotificationsGranted()
    }

    override fun onPostNotificationsDenied() {
        viewModel.notifyPostNotificationsDenied()
    }
}
