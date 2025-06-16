package commons.android

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
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
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
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
