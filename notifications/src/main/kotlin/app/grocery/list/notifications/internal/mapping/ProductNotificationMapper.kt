package app.grocery.list.notifications.internal.mapping

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import app.grocery.list.domain.Product
import app.grocery.list.notifications.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

internal class ProductNotificationMapper @AssistedInject constructor(
    @Assisted
    private val params: Params,
    @ApplicationContext
    private val context: Context,
    private val getGroupKeyOfCategory: GetGroupKeyOfProductCategory,
) {
    fun transform(product: Product): Notification =
        NotificationCompat.Builder(context, params.channelId)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setContentTitle(product.title)
            .setGroup(getGroupKeyOfCategory.execute(categoryId = product.categoryId))
            .build()

    data class Params(
        val channelId: String,
    )

    @AssistedFactory
    abstract class Factory {
        abstract fun create(params: Params): ProductNotificationMapper
        fun create(channelId: String): ProductNotificationMapper {
            val params = Params(channelId = channelId)
            return create(params)
        }
    }
}
