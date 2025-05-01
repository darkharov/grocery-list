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

internal class CategoryNotificationMapper @AssistedInject constructor(
    @Assisted
    private val params: Params,
    @ApplicationContext
    private val context: Context,
    private val getGroupKeyOfProductCategory: GetGroupKeyOfProductCategory,
) {
    fun transform(category: Product.Category): Notification =
        NotificationCompat.Builder(context, params.channelId)
            .setSmallIcon(R.drawable.ic_stat_logo)
            .setStyle(style(category))
            .setGroupSummary(true)
            .setGroup(getGroupKeyOfProductCategory.execute(categoryId = category.id))
            .build()

    private fun style(category: Product.Category): NotificationCompat.BigTextStyle =
        NotificationCompat
            .BigTextStyle()
            .setSummaryText(category.title.uppercase())
            .bigText(category.title.uppercase())

    data class Params(
        val channelId: String,
    )

    @AssistedFactory
    abstract class Factory {
        abstract fun create(params: Params): CategoryNotificationMapper
        fun create(channelId: String): CategoryNotificationMapper {
            val params = Params(channelId = channelId)
            return create(params)
        }
    }
}
