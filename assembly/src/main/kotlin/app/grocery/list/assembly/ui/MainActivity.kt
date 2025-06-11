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
import app.grocery.list.assembly.ui.content.AppDelegatesFacade
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.notifications.NotificationPublisher
import commons.android.ApplicationActivityMarker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    ApplicationActivityMarker,
    AppDelegatesFacade {

    override val isSplashScreen = true

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher

    private val viewModel by viewModels<MainViewModel>()
    private val postNotifications = postNotificationLauncher()

    private fun postNotificationLauncher() =
        registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                postNotificationsAndExitFromApp()
            }
        }

    private fun postNotificationsAndExitFromApp() {
        lifecycleScope.launch {
            val productList = viewModel.productList.filterNotNull().first()
            notificationPublisher.tryToPost(productList)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val numberOfAddedProducts by viewModel.numberOfAddedProducts.collectAsState()
            val navController = rememberNavController()
            themeUtil.GroceryListTheme {
                AppContent(
                    numberOfAddedProducts = numberOfAddedProducts,
                    navController = navController,
                    delegates = this,
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
            postNotificationsAndExitFromApp()
        }
    }
}
