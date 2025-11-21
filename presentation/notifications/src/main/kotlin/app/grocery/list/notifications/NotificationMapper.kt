package app.grocery.list.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import app.grocery.list.domain.notification.NotificationContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationMapper @Inject constructor(
    @param:ApplicationContext
    private val context: Context,
) {
    fun transform(item: NotificationContent): Notification =
        NotificationCompat
            .Builder(context, NotificationConfigs.DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setGroup(item.groupKey.toString())
            .setContentTitle(item.formattedProductTitles)
            .setDeleteIntent(deleteIntent(productIds = item.productIds))
            .build()

    private fun deleteIntent(productIds: List<Int>): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            productIds.first(),
            Intent(context, NotificationDismissReceiver::class.java)
                .putExtra(NotificationDismissReceiver.EXTRA_PRODUCT_IDS, productIds.toIntArray()),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT,
        )
}
