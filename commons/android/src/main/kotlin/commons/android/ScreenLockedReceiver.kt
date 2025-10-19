package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ScreenLockedReceiver private constructor(
    private val onScreenLocked: ScreenLockedReceiver.() -> Unit,
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
            val receiver = ScreenLockedReceiver(
                onScreenLocked = {
                    onScreenLocked()
                    activity.unregisterReceiver(this)   // sequential clicks on power button are unnecessary
                }
            )
            val intentFilter = IntentFilter(TARGET_ACTION)
            activity.lifecycle.addObserver(
                observer = object : DefaultLifecycleObserver {

                    override fun onResume(owner: LifecycleOwner) {
                        activity.registerReceiver(receiver, intentFilter)
                    }

                    override fun onDestroy(owner: LifecycleOwner) {
                        activity.unregisterReceiver(receiver)
                    }
                }
            )
        }
    }
}
