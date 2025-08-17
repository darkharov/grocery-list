package app.grocery.list.assembly.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import app.grocery.list.assembly.BuildConfig
import app.grocery.list.assembly.R
import app.grocery.list.assembly.ui.content.AppContent
import app.grocery.list.assembly.ui.content.AppContentDelegate
import app.grocery.list.commons.compose.theme.ThemeUtil
import app.grocery.list.commons.format.ProductListToStringFormatter
import app.grocery.list.domain.Product
import app.grocery.list.final_.steps.FinalSteps
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

    @Inject lateinit var themeUtil: ThemeUtil
    @Inject lateinit var notificationPublisher: NotificationPublisher
    @Inject lateinit var productListToStringFormatter: ProductListToStringFormatter

    private val viewModel by viewModels<MainViewModel>()
    private val permissionUtil = PermissionUtil()
    private var currentDestination: NavDestination? = null

    override val appVersionName: String = BuildConfig.VERSION_NAME

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
            themeUtil.GroceryListTheme {
                AppContent(
                    numberOfEnabledProducts = numberOfEnabledProducts,
                    progress = progress,
                    hasEmojiIfEnoughSpace = hasEmojiIfEnoughSpace,
                    delegates = this,
                    appEvents = viewModel.appEvents(),
                    snackbars = viewModel.snackbars(),
                )
            }
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

    override fun exitFromApp() {
        finish()
    }

    override fun startShopping() {
        permissionUtil.requestPostNotifications()
    }

    override fun onPostNotificationsGranted() {
        viewModel.notifyPushNotificationsGranted()
    }

    override fun handleCurrentDestinationChange(newValue: NavDestination) {
        currentDestination = newValue
    }

    override fun share(products: List<Product>) {
        val suffix = getString(
            R.string.sharing_message_suffix,
            getString(R.string.paste_copied_list),
            getString(R.string.actions),
            "https://play.google.com/store/apps/details?id=app.grocery.list",
        )
        val text = productListToStringFormatter.print(products, suffix = suffix)
        share(text = text)
    }

    override fun contactSupport() {
        email(
            email = "product.list.supp@gmail.com",
            subject = "Android, app.grocery.list",
            text =
                "\n\n\n\n" +
                "\nAndroid version: ${Build.VERSION.RELEASE} " +
                "(API level ${Build.VERSION.SDK_INT})" +
                "\nVersion Code: ${BuildConfig.VERSION_CODE}" +
                "\nVersion Name: ${BuildConfig.VERSION_NAME}" +
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
}
