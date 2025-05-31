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
import app.grocery.list.domain.Product
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
    categoryNotificationMapperFactory: CategoryNotificationMapper.Factory,
    productNotificationMapperFactory: ProductNotificationMapper.Factory,
) {
    private val productMapper = productNotificationMapperFactory.create(channelId = DEFAULT_CHANNEL_ID)
    private val categoryMapper = categoryNotificationMapperFactory.create(channelId = DEFAULT_CHANNEL_ID)

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
        val allProducts = productList.flatMap { it.products }
        if (allProducts.size <= MAX_VISIBLE_AT_THE_SAME_TIME) {
            postProducts(allProducts)
        } else {
            postGroupsAndProducts(productList)
        }
    }

    private fun postGroupsAndProducts(productList: List<CategoryAndProducts>) {
        for ((category, products) in productList.reversed()) {
            notificationManager.notify(
                TYPE_CATEGORY,
                category.id,
                categoryMapper.transform(category),
            )
            postProducts(products)
        }
    }

    private fun postProducts(products: List<Product>) {
        for (product in products.reversed()) {
            notificationManager.notify(
                TYPE_PRODUCT,
                product.id,
                productMapper.transform(product),
            )
        }
    }

    private companion object {
        private const val DEFAULT_CHANNEL_ID = "app.wifeslist.notifications.DEFAULT_CHANNEL_ID"
        private const val TYPE_CATEGORY = "app.wifeslist.notifications.TYPE_CATEGORY"
        private const val TYPE_PRODUCT = "app.wifeslist.notifications.TYPE_PRODUCT"

        // Pure Android, determined experimentally
        // TODO: move this constant to resources. It may have different value in different versions of Android.
        private const val MAX_VISIBLE_AT_THE_SAME_TIME = 8
    }
}
