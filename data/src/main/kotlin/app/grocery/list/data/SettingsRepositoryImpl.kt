package app.grocery.list.data

import app.grocery.list.domain.BottomBarRoadmapStep
import app.grocery.list.domain.SettingsRepository
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.storage.value.android.StorageValueDelegates
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsRepositoryImpl @Inject constructor(
    delegates: StorageValueDelegates,
) : SettingsRepository {

    override val productTitleFormatter =
        delegates.enum(
            defaultValue = ProductTitleFormatter.EmojiAndFullText,
            keyPrefix = "$OLD_KEY_PREFIX\$productTitleFormatter",
        )

    override val clearNotificationsReminderEnabled =
        delegates.boolean(
            defaultValue = true,
            key = "$OLD_KEY_PREFIX\$clearNotificationsReminderEnabled",
        )

    override val bottomBarRoadmapStep =
        delegates.enum(
            defaultValue = BottomBarRoadmapStep.Initial,
            keyPrefix = "$OLD_KEY_PREFIX\$bottomBarRoadmapStep",
        )

    override val recommendAppWhenSharingList =
        delegates.boolean(
            defaultValue = true,
            key = "$OLD_KEY_PREFIX\$recommendAppWhenSharingList",
        )

    companion object {
        const val OLD_KEY_PREFIX = "app.grocery.list.data.AppRepositoryImpl"
    }
}
