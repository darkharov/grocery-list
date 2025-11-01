package app.grocery.list.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.domain.HandleProductListPostedUseCase
import app.grocery.list.domain.format.FormatProductsForNotificationsUseCase
import app.grocery.list.domain.format.NotificationContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val formatProductsForNotifications: FormatProductsForNotificationsUseCase,
    private val notificationManager: NotificationManagerCompat,
    private val handleProductListPublished: HandleProductListPostedUseCase,
) {
    init {
        ensureDefaultNotificationChannel()
    }

    private fun ensureDefaultNotificationChannel() {
        val defaultChannel = NotificationChannel(
            DEFAULT_CHANNEL_ID,
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
            val content = formatProductsForNotifications()
            post(content)
            handleProductListPublished.execute()
        }
    }

    private suspend fun formatProductsForNotifications() =
        formatProductsForNotifications
            .execute(
                maxNumberOfItems = MAX_VISIBLE_AT_THE_SAME_TIME,
            )

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(
        content: List<NotificationContent>,
    ) {
        val reversed = content.reversed()   // the first notification will be the last
        for (item in reversed) {
            val notification = notification(item)
            notificationManager.notify(TYPE_PRODUCT, item.groupKey, notification)
        }
    }

    private fun notification(item: NotificationContent): Notification =
        NotificationCompat
            .Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setGroup(item.groupKey.toString())
            .setContentTitle(item.formattedProductTitles)
            .setDeleteIntent(deleteIntent(productIds = item.productIds))
            .build()

    private fun deleteIntent(productIds: List<Int>) =
        PendingIntent.getBroadcast(
            context,
            productIds.first(),
            Intent(context, NotificationDismissReceiver::class.java)
                .putExtra(NotificationDismissReceiver.EXTRA_PRODUCT_IDS, productIds.toIntArray()),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
        )

    companion object {
        private const val DEFAULT_CHANNEL_ID = "app.grocery.list.notifications.DEFAULT_CHANNEL_ID"
        private const val TYPE_PRODUCT = "app.grocery.list.notifications.TYPE_PRODUCT"

        // determined experimentally
        private const val MAX_VISIBLE_AT_THE_SAME_TIME = 8
    }
}
