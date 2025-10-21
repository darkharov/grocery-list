package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
            val processLifecycleObserver = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    activity.registerReceiver(receiver, IntentFilter(TARGET_ACTION))
                }
                override fun onPause(owner: LifecycleOwner) {
                    activity.unregisterReceiver(receiver)
                }
            }
            processLifecycle.addObserver(processLifecycleObserver)
            activity.lifecycle.addObserver(
                observer = object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        processLifecycle.removeObserver(processLifecycleObserver)
                    }
                }
            )
        }
    }
}
