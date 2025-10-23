package commons.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

class SafeBroadcastRegisterer(
    private val context: Context,
    private val receiver: BroadcastReceiver,
    private val action: String,
) {
    private var registered = false

    fun unregister() {
        if (registered) {
            context.unregisterReceiver(receiver)
            registered = false
        }
    }

    fun register() {
        if (!(registered)) {
            context.registerReceiver(receiver, IntentFilter(action))
            registered = true
        }
    }
}
