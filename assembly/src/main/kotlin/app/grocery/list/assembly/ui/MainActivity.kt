package app.grocery.list.assembly.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import app.grocery.list.assembly.ui.content.AppContent
import app.grocery.list.assembly.ui.content.AppContentDelegate
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.notifications.NotificationPublisher
import app.grocery.list.preparing.for_.shopping.PreparingForShopping
import commons.android.ApplicationActivityMarker
import commons.android.ScreenLockedReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    ApplicationActivityMarker,
    AppContentDelegate {

    override val isSplashScreen = true

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher

    private val viewModel by viewModels<MainViewModel>()
    private val postNotifications = postNotificationLauncher()
    private var currentRoute: String? = null

    private fun postNotificationLauncher() =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                viewModel.notifyPushNotificationsGranted()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupContent()
        observeScreenLock()
    }

    private fun observeScreenLock() {
        ScreenLockedReceiver.register(this) {
            if (currentRoute == PreparingForShopping) {
                lifecycleScope.launch {
                    val productList = viewModel.productList.filterNotNull().first()
                    notificationPublisher.tryToPost(productList)
                    finish()
                }
            }
        }
    }

    private fun setupContent() {
        setContent {
            val numberOfAddedProducts by viewModel.numberOfAddedProducts.collectAsState()
            themeUtil.GroceryListTheme {
                AppContent(
                    numberOfAddedProducts = numberOfAddedProducts,
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

    override fun onScreenChange(route: String?) {
        currentRoute = route
    }
}
