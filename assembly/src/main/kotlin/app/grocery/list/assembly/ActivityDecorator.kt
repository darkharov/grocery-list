package app.grocery.list.assembly

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import app.grocery.list.commons.app.ApplicationActivityMarker
import app.grocery.list.commons.compose.theme.DarkBackground
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityDecorator @Inject constructor(
    private val application: Application,
) {
    fun onApplicationCreate() {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
    }

    private fun Activity.applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        this as ComponentActivity
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.WHITE,
                darkScrim = DarkBackground.toArgb(),
            ),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }


    private inner class ActivityLifecycleCallbacksImpl : CustomActivityLifecycleCallbacks() {

        override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity is ApplicationActivityMarker) {
                if (activity.isSplashScreen) {
                    activity.installSplashScreen()
                }
                // Do not reorder!
                // Splash screen should be installed before setting edge to edge
                activity.applyEdgeToEdge()
            }
        }
    }
}
