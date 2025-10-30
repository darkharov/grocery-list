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
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.HandleProductListPostedUseCase
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.format.printToString
import app.grocery.list.storage.value.kotlin.get
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AppRepository,
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
            post(
                formatter = repository
                    .productTitleFormatter
                    .get(),
                categorizedProducts = repository
                    .categorizedProducts(
                        criteria = AppRepository.CategorizedProductsCriteria.EnabledOnly,
                    )
                    .first(),
            )
            handleProductListPublished.execute()
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(
        formatter: ProductTitleFormatter,
        categorizedProducts: List<CategoryAndProducts>,
    ) {
        val allProducts = categorizedProducts.flatMap { it.products }
        val maxItemsPerNotification = 1 + (allProducts.size - 1) / MAX_VISIBLE_AT_THE_SAME_TIME

        for (group in allProducts.chunked(maxItemsPerNotification).reversed()) {
            val groupKey = group.first().id
            val productIds = group.map { it.id }
            val sortedGroup = group.sortedBy { it.emojiSearchResult != null }
            val notification = notification(
                groupKey = groupKey,
                productIds = productIds,
                contentTitle = formatter.printToString(sortedGroup),
            )
            notificationManager.notify(TYPE_PRODUCT, groupKey, notification)
        }
    }

    private fun notification(
        groupKey: Int,
        productIds: List<Int>,
        contentTitle: String,
    ): Notification =
        NotificationCompat
            .Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setGroup(groupKey.toString())
            .setContentTitle(contentTitle)
            .setDeleteIntent(deleteIntent(productIds = productIds))
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
