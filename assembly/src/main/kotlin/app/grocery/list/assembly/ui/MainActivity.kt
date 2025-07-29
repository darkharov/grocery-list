package app.grocery.list.assembly.ui

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import app.grocery.list.assembly.R
import app.grocery.list.assembly.ui.content.AppContent
import app.grocery.list.assembly.ui.content.AppContentDelegate
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.commons.format.ProductListToStringFormatter
import app.grocery.list.domain.Product
import app.grocery.list.final_.steps.FinalSteps
import app.grocery.list.notifications.NotificationPublisher
import commons.android.ScreenLockedReceiver
import commons.android.share
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    AppContentDelegate {

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher
    @Inject lateinit var productListToStringFormatter: ProductListToStringFormatter

    private val viewModel by viewModels<MainViewModel>()
    private val postNotifications = postNotificationLauncher()
    private var currentDestination: NavDestination? = null

    private fun postNotificationLauncher() =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                viewModel.notifyPushNotificationsGranted()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        setupContent()
        observeScreenLock()
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

    private fun observeScreenLock() {
        ScreenLockedReceiver.register(this) {
            if (currentDestination?.hasRoute(FinalSteps::class) == true) {
                notificationPublisher.tryToPost()
                finish()
            }
        }
    }

    private fun setupContent() {
        setContent {
            val numberOfAddedProducts by viewModel.numberOfAddedProducts.collectAsState()
            val progress by viewModel.progress.collectAsState()
            val hasEmojiIfEnoughSpace by viewModel.hasEmojiIfEnoughSpace.collectAsState()
            themeUtil.GroceryListTheme {
                AppContent(
                    numberOfAddedProducts = numberOfAddedProducts,
                    progress = progress,
                    hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                    delegates = this,
                    appEvents = viewModel.appEvents(),
                )
            }
        }
    }

    override fun onExitFromApp() {
        finish()
    }

    override fun onStartShopping() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            postNotifications.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.notifyPushNotificationsGranted()
        }
    }

    override fun onCurrentDestinationChange(newValue: NavDestination) {
        currentDestination = newValue
    }

    override fun share(products: List<Product>) {
        val suffix = getString(
            R.string.sharing_message_suffix,
            getString(R.string.actions),
            "https://play.google.com/store/apps/details?id=app.grocery.list",
        )
        val text = productListToStringFormatter.print(products, suffix = suffix)
        share(text = text)
    }
}
