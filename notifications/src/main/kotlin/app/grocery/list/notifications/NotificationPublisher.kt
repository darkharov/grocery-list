package app.grocery.list.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import app.grocery.list.domain.CategoryAndProducts
import app.grocery.list.notifications.internal.mapping.CategoryNotificationMapper
import app.grocery.list.notifications.internal.mapping.ProductNotificationMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationPublisher @Inject internal constructor(
    @ApplicationContext
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val categoryNotificationMapperFactory: CategoryNotificationMapper.Factory,
    private val productNotificationMapperFactory: ProductNotificationMapper.Factory,
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
            post(productsList)
            true
        } else {
            false
        }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun post(groceryList: List<CategoryAndProducts>) {
        notificationManager.cancelAll()
        val categoryMapper = CategoryNotificationMapper()
        for ((category, products) in groceryList.reversed()) {
            notificationManager.notify(
                TYPE_CATEGORY,
                category.id,
                categoryMapper.transform(category),
            )
            val productMapper = ProductNotificationMapper()
            for (product in products.reversed()) {
                notificationManager.notify(
                    TYPE_PRODUCT,
                    product.id,
                    productMapper.transform(product),
                )
            }
        }
    }

    private fun CategoryNotificationMapper() =
        categoryNotificationMapperFactory.create(channelId = DEFAULT_CHANNEL_ID)

    private fun ProductNotificationMapper() =
        productNotificationMapperFactory.create(channelId = DEFAULT_CHANNEL_ID)

    private companion object {
        private const val DEFAULT_CHANNEL_ID = "app.wifeslist.notifications.DEFAULT_CHANNEL_ID"
        private const val TYPE_CATEGORY = "app.wifeslist.notifications.TYPE_CATEGORY"
        private const val TYPE_PRODUCT = "app.wifeslist.notifications.TYPE_PRODUCT"
    }
}
