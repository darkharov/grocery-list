package app.grocery.list.assembly

import android.app.Application
import app.grocery.list.notifications.NotificationPublisher
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationImpl : Application() {

    @Inject lateinit var notificationPublisher: NotificationPublisher

    override fun onCreate() {
        super.onCreate()
        notificationPublisher.onApplicationCreate()
    }
}
