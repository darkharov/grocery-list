package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ScreenLockedReceiver private constructor(
    private val onScreenLocked: () -> Unit,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TARGET_ACTION) {
            onScreenLocked()
        }
    }

    companion object {
        private const val TARGET_ACTION = Intent.ACTION_SCREEN_OFF
        fun register(
            activity: ComponentActivity,
            onScreenLocked: () -> Unit,
        ) {
            val intentFilter = IntentFilter(TARGET_ACTION)
            val receiver = ScreenLockedReceiver(onScreenLocked = onScreenLocked)
            activity.registerReceiver(receiver, intentFilter)
            activity.lifecycle.addObserver(
                observer = object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        activity.unregisterReceiver(receiver)
                    }
                }
            )
        }
    }
}
