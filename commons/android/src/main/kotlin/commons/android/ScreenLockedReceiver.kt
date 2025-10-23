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
import androidx.lifecycle.ProcessLifecycleOwner

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
            val receiver = ScreenLockedReceiver(onScreenLocked = onScreenLocked)

            val processLifecycle = ProcessLifecycleOwner.get().lifecycle
            val processLifecycleObserver = ProcessLifecycleObserverImpl(activity, receiver)
            processLifecycle.addObserver(processLifecycleObserver)

            activity.lifecycle.addObserver(
                observer = object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        processLifecycle.removeObserver(processLifecycleObserver)
                    }
                }
            )
        }

        private class ProcessLifecycleObserverImpl(
            private val activity: ComponentActivity,
            private val receiver: ScreenLockedReceiver,
        ) : DefaultLifecycleObserver {

            private val handler = Handler(Looper.getMainLooper())
            private val unregisterReceiver = Runnable { activity.unregisterReceiver(receiver) }

            override fun onResume(owner: LifecycleOwner) {
                handler.removeCallbacks(unregisterReceiver)
                activity.registerReceiver(receiver, IntentFilter(TARGET_ACTION))
            }

            override fun onPause(owner: LifecycleOwner) {
                handler.postDelayed(unregisterReceiver, 250L)
            }
        }
    }
}
