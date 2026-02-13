package app.grocery.list.assembly

import android.app.Application
import android.content.IntentFilter
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationImpl : Application() {

    @Inject
    internal lateinit var screenLockReceiver: ScreenLockReceiver

    override fun onCreate() {
        super.onCreate()
        registerReceiver(
            screenLockReceiver,
            IntentFilter(ScreenLockReceiver.TARGET_ACTION)
        )
    }
}
