package commons.android

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
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
 *      fun myMethod() {
 *          permissionUtil.requestPostNotifications()
 *      }
 *
 *      override fun onPostNotificationsGranted() {
 *          ...
 *      }
 *
 *      override fun onPostNotificationsDenied() {
 *          ...
 *          MyDialog(
 *              ...
 *              onGoToSettings = {
 *                   permissionUtil.openNotificationSettings()
 *              }
 *          )
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

    fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
        activity.startActivity(intent)
    }

    interface Contract {
        fun onPostNotificationsGranted() {}
        fun onPostNotificationsDenied() {
            throw UnsupportedOperationException("onPostNotificationsDenied() is not implemented")
        }
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
