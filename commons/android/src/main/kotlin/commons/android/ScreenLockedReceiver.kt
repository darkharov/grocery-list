package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
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
            val receiver = ScreenLockedReceiver(
                onScreenLocked = onScreenLocked,
            )
            activity.lifecycle.addObserver(
                LifecycleObserverImpl(activity, receiver),
            )
        }

        private class LifecycleObserverImpl(
            private val activity: ComponentActivity,
            private val receiver: BroadcastReceiver,
        ) : DefaultLifecycleObserver {

            private val doUnregister = { tryToUnregister() }
            private val handler = Handler(Looper.getMainLooper())
            private var registered = false

            private fun tryToUnregister() {
                if (registered) {
                    activity.unregisterReceiver(receiver)
                    registered = false
                }
            }

            private fun tryToRegister() {
                if (!(registered)) {
                    activity.registerReceiver(receiver, IntentFilter(TARGET_ACTION))
                    registered = true
                }
            }

            override fun onResume(owner: LifecycleOwner) {
                handler.removeCallbacks(doUnregister)
                tryToRegister()
            }

            override fun onPause(owner: LifecycleOwner) {
                if (activity.isChangingConfigurations) {
                    // immediately to avoid leaks
                    doUnregister()
                } else {
                    // event can be triggered after some time after onPause()
                    handler.postDelayed(doUnregister, 1_000)
                }
            }
        }
    }
}
