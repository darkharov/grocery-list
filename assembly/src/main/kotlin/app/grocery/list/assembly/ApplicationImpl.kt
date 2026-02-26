package app.grocery.list.assembly

import android.app.Application
import app.grocery.list.data.InitData
import app.grocery.list.notifications.NotificationPublisher
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationImpl : Application() {

    @Inject lateinit var initData: InitData
    @Inject lateinit var notificationPublisher: NotificationPublisher

    override fun onCreate() {
        super.onCreate()
        initData.execute()
        notificationPublisher.onApplicationCreate()
    }
}
