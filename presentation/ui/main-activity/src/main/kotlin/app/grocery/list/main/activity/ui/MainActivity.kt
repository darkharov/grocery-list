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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.domain.product.Product
import app.grocery.list.main.activity.ui.content.AppContent
import app.grocery.list.main.activity.ui.content.AppContentDelegate
import app.grocery.list.main.activity.ui.content.FinalSteps
import app.grocery.list.notifications.NotificationPublisher
import commons.android.PermissionUtil
import commons.android.ScreenLockedReceiver
import commons.android.email
import commons.android.share
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    ComponentActivity(),
    AppContentDelegate,
    PermissionUtil.Contract {

    @Inject lateinit var contract: Contract
    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher

    private val viewModel by viewModels<MainViewModel>()
    private val permissionUtil = PermissionUtil()
    private var currentScreenKey: NavKey? = null

    override val appVersionName get() = contract.versionName

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

    private fun setupContent() {
        setContent {
            val numberOfEnabledProducts by viewModel.numberOfEnabledProducts.collectAsState()
            val progress by viewModel.progress.collectAsState()
            val hasEmojiIfEnoughSpace by viewModel.hasEmojiIfEnoughSpace.collectAsState()
            val dialog by viewModel.dialog().collectAsStateWithLifecycle()
            themeUtil.GroceryListTheme {
                AppContent(
                    numberOfEnabledProducts = numberOfEnabledProducts,
                    progress = progress,
                    hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                    dialog = dialog,
                    delegate = this,
                    appEvents = viewModel.appEvents(),
                    snackbars = viewModel.snackbars(),
                )
            }
        }
    }

    private fun observeScreenLock() {
        ScreenLockedReceiver.register(this) {
            if (currentScreenKey is FinalSteps) {
                notificationPublisher.tryToPost()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notificationPublisher.cancelAllNotifications()
    }

    override fun exitFromApp() {
        finish()
    }

    override fun startShopping() {
        permissionUtil.requestPostNotifications()
    }

    override fun onPostNotificationsGranted() {
        viewModel.notifyPushNotificationsGranted()
    }

    override fun onPostNotificationsDenied() {
        viewModel.notifyPostNotificationsDenied()
    }

    override fun handleCurrentScreenChange(newValue: NavKey) {
        currentScreenKey = newValue
    }

    override fun shareProducts(sharingString: String) {
        share(text = sharingString)
    }

    override fun contactSupport() {
        email(
            email = "product.list.supp@gmail.com",
            subject = "Android, app.grocery.list",
            text =
                "\n\n\n\n" +
                "\nAndroid version: ${Build.VERSION.RELEASE} " +
                "(API level ${Build.VERSION.SDK_INT})" +
                "\nVersion Code: ${contract.versionCode}" +
                "\nVersion Name: ${contract.versionName}" +
                "\nBrand: ${Build.BRAND}" +
                "\nModel: ${Build.MODEL}",
        )
    }

    override fun showUndoProductDeletionSnackbar(product: Product) {
        viewModel.showUndoProductDeletionSnackbar(product)
    }

    override fun undoProductDeletion(product: Product) {
        viewModel.undoProductDeletion(product)
    }

    override fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }

    override fun handleDialogDismiss() {
        viewModel.notifyDialogDismiss()
    }

    interface Contract {
        val versionName: String
        val versionCode: Int
    }
}
