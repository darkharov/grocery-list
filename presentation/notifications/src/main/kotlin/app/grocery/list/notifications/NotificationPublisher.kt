package app.grocery.list.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.domain.HandleProductListPostedUseCase
import app.grocery.list.domain.format.notification.GetNotificationsUseCase
import app.grocery.list.domain.format.notification.NotificationContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val mapper: NotificationMapper,
    private val getNotifications: GetNotificationsUseCase,
    private val notificationManager: NotificationManagerCompat,
    private val handleProductListPublished: HandleProductListPostedUseCase,
) {
    init {
        ensureDefaultNotificationChannel()
    }

    private fun ensureDefaultNotificationChannel() {
        val defaultChannel = NotificationChannel(
            NotificationConfigs.DEFAULT_CHANNEL_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(defaultChannel)
    }

    fun tryToPost(): Boolean =
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            cancelAllNotifications()
            post()
            true
        } else {
            false
        }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post() {
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            val maxNumberOfItems = NotificationConfigs.MAX_VISIBLE_AT_THE_SAME_TIME
            val notifications = getNotifications.execute(maxNumberOfItems = maxNumberOfItems)
            post(notifications)
            handleProductListPublished.execute()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(notifications: List<NotificationContent>) {
        val reversed = notifications.reversed() // The last ones rise to the top, but we need opposite behavior
        for (notification in reversed) {
            val androidNotification = mapper.transform(notification)
            notificationManager.notify(
                NotificationConfigs.TYPE_PRODUCT,
                notification.groupKey,
                androidNotification,
            )
        }
    }
}
