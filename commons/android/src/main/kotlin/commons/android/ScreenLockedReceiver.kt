package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
            activity: ComponentActivity,
            receiver: BroadcastReceiver,
        ) : DefaultLifecycleObserver {

            private val doUnregister = Runnable { registerer.unregister() }
            private val handler = Handler(Looper.getMainLooper())

            private val registerer = SafeBroadcastRegisterer(
                context = activity,
                receiver = receiver,
                action = TARGET_ACTION,
            )

            override fun onResume(owner: LifecycleOwner) {
                handler.removeCallbacks(doUnregister)
                registerer.register()
            }

            override fun onPause(owner: LifecycleOwner) {
                handler.postDelayed(doUnregister, 1_000)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                handler.removeCallbacks(doUnregister)
                registerer.unregister()
            }
        }
    }
}
