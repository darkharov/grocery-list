package app.grocery.list.data

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.BottomBarRoadmapStep
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.storage.value.android.StorageValueDelegates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppRepositoryImpl @Inject constructor(
    delegates: StorageValueDelegates,
) : AppRepository {

    override val productTitleFormatter =
        delegates.enum(
            defaultValue = ProductTitleFormatter.EmojiAndFullText,
            keyPrefix = "$KEY\$productTitleFormatter",
        )

    override val clearNotificationsReminderEnabled =
        delegates.boolean(
            defaultValue = true,
            key = "$KEY\$clearNotificationsReminderEnabled",
        )

    override val bottomBarRoadmapStep =
        delegates.enum(
            defaultValue = BottomBarRoadmapStep.Initial,
            keyPrefix = "$KEY\$bottomBarRoadmapStep",
        )

    override val recommendAppWhenSharingList =
        delegates.boolean(
            defaultValue = true,
            key = "$KEY\$recommendAppWhenSharingList",
        )

    companion object {
        const val KEY = "app.grocery.list.data.AppRepositoryImpl"
    }
}
