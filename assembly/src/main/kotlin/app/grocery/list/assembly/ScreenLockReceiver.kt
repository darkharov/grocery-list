package app.grocery.list.assembly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import app.grocery.list.domain.ScreenLockEventBuffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ScreenLockReceiver @Inject constructor(
    private val screenLockEventBuffer: ScreenLockEventBuffer,
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TARGET_ACTION) {
            screenLockEventBuffer.notifyScreenLocked()
        }
    }

    companion object {
        const val TARGET_ACTION = Intent.ACTION_SCREEN_OFF
    }
}
