package app.grocery.list.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.commons.format.ProductTitleFormatter
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.domain.Product
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AppRepository,
    private val notificationManager: NotificationManagerCompat,
    private val productTitleFormatterFactory: ProductTitleFormatter.Factory,
) {
    init {
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
            notificationManager.cancelAll()
            post()
            true
        } else {
            false
        }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post() {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            post(
                formatter = repository
                    .productItemFormat()
                    .map(productTitleFormatterFactory::create)
                    .flowOn(Dispatchers.IO)
                    .first(),
                categorizedProducts = repository
                    .categorizedProducts()
                    .flowOn(Dispatchers.IO)
                    .first(),
            )
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(
        formatter: ProductTitleFormatter,
        categorizedProducts: List<CategoryAndProducts>,
    ) {
        val allProducts = categorizedProducts.flatMap { it.products }
        val maxItemsPerNotification = 1 + (allProducts.size - 1) / MAX_VISIBLE_AT_THE_SAME_TIME

        for (chunk in allProducts.chunked(maxItemsPerNotification).reversed()) {
            val groupKey = chunk.first().id
            val notification = notification(chunk, formatter, groupKey = groupKey)
            notificationManager.notify(TYPE_PRODUCT, groupKey, notification)
        }
    }

    private fun notification(
        chunk: List<Product>,
        formatter: ProductTitleFormatter,
        groupKey: Int,
    ): Notification =
        NotificationCompat
            .Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setContentTitle(
                chunk
                    .sortedBy { it.emojiSearchResult != null }
                    .joinToString { product ->
                        formatter.print(product).collectTitle()
                    }
            )
            .setGroup(groupKey.toString())
            .build()

    private fun ProductTitleFormatter.FormattingResult.collectTitle(): String =
        buildString {
            if (!(emoji.isNullOrBlank())) {
                append(emoji)
                append(' ')
            }
            val additionalDetailsLocation = additionalDetailsLocation
            val title = if (additionalDetailsLocation != null) {
                title.removeRange(
                    startIndex = additionalDetailsLocation.startIndex,
                    endIndex = additionalDetailsLocation.endIndex,
                )
            } else {
                title
            }
            append(title)
        }

    private companion object {
        private const val DEFAULT_CHANNEL_ID = "app.grocery.list.notifications.DEFAULT_CHANNEL_ID"
        private const val TYPE_PRODUCT = "app.grocery.list.notifications.TYPE_PRODUCT"

        // determined experimentally
        private const val MAX_VISIBLE_AT_THE_SAME_TIME = 8
    }
}
