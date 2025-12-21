package commons.android

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi

/**
 * Slightly reduces amount of boilerplate code that handles permission requests.
 *
 * # Example of usage:
 *
 * ```
 *  class MyActivity : ComponentActivity(), PermissionUtil.Contract {
 *
 *      val permissionUtil = PermissionUtil()
 *
 *      private fun itIsTimeToCheckAbilityToPostNotifications() {
 *          permissionUtil.requestPostNotifications()
 *      }
 *
 *      override fun onPostNotificationsGranted() {
 *          ...
 *      }
 *      override fun onPostNotificationsDenied() {
 *          ...
 *      }
 *  }
 * ```
 */
class PermissionUtil<T> internal constructor(
    private val activity: T,
) where T : ComponentActivity,
        T : PermissionUtil.Contract {

    private val contract = (activity as Contract)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val postNotifications = PostNotifications()

    fun requestPostNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            postNotifications.launch()
        } else {
            contract.onPostNotificationsGranted()
        }
    }

    interface Contract {
        fun onPostNotificationsGranted() {}
        fun onPostNotificationsDenied() {}
    }

    private open inner class LauncherHolder(
        private val permission: String,
        private val onGranted: () -> Unit,
        private val onDenied: () -> Unit,
    ) {
        private val launcher = activity.registerForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                onGranted()
            } else {
                onDenied()
            }
        }

        fun launch() {
            launcher.launch(permission)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private inner class PostNotifications : LauncherHolder(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        onGranted = {
            contract.onPostNotificationsGranted()
        },
        onDenied = {
            contract.onPostNotificationsDenied()
        },
    )
}

fun <T> T.PermissionUtil() where T : ComponentActivity, T : PermissionUtil.Contract =
    PermissionUtil(this)
