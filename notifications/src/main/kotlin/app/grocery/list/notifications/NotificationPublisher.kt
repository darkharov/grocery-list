package app.grocery.list.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import app.grocery.list.domain.CategoryAndProducts
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
) {
    init {
        val defaultChannel = NotificationChannel(
            DEFAULT_CHANNEL_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        notificationManager.createNotificationChannel(defaultChannel)
    }

    fun tryToPost(productsList: List<CategoryAndProducts>): Boolean =
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.cancelAll()
            post(productsList)
            true
        } else {
            false
        }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(productList: List<CategoryAndProducts>) {

        val allProducts = productList.reversed().flatMap { it.products }
        val chunkSize = 1 + (allProducts.size - 1) / MAX_VISIBLE_AT_THE_SAME_TIME

        for (chunk in allProducts.chunked(chunkSize)) {
            val notification = NotificationCompat
                .Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_logo)
                .setContentTitle(
                    chunk
                        .reversed()
                        .joinToString {
                            "${it.emoji.orEmpty()} ${it.title}".trim()
                        }
                )
                .setGroup(chunk.first().id.toString())
                .build()
            notificationManager.notify(TYPE_PRODUCT, chunk.first().id, notification)
        }
    }

    private companion object {
        private const val DEFAULT_CHANNEL_ID = "app.grocery.list.notifications.DEFAULT_CHANNEL_ID"
        private const val TYPE_PRODUCT = "app.grocery.list.notifications.TYPE_PRODUCT"

        // determined experimentally
        private const val MAX_VISIBLE_AT_THE_SAME_TIME = 8
    }
}
