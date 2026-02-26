package app.grocery.list.data.settings

import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsRepositoryImpl @Inject constructor(
    storageValueFactory: StorageValueFactory,
) : SettingsRepository {

    override val productTitleFormat =
        storageValueFactory.enum(
            key = PRODUCT_TITLE_FORMATTER,
            defaultValue = ProductTitleFormat.EmojiAndFullText
        )

    override val clearNotificationsReminderEnabled =
        storageValueFactory.boolean(
            key = CLEAR_NOTIFICATIONS_REMINDER_ENABLED,
            defaultValue = true,
        )

    override val bottomBarRoadmapStep =
        storageValueFactory.enum(
            key = BOTTOM_BAR_ROADMAP_STEP,
            defaultValue = BottomBarRoadmapStep.Initial,
        )

    override val recommendAppWhenSharingList =
        storageValueFactory.boolean(
            key = RECOMMEND_APP_WHEN_SHARING_LIST,
            defaultValue = true,
        )

    companion object {

        const val PRODUCT_TITLE_FORMATTER = "app.grocery.list.data.settings.PRODUCT_TITLE_FORMATTER"
        const val BOTTOM_BAR_ROADMAP_STEP = "app.grocery.list.data.settings.BOTTOM_BAR_ROADMAP_STEP"
        const val RECOMMEND_APP_WHEN_SHARING_LIST = "app.grocery.list.data.settings.RECOMMEND_APP_WHEN_SHARING_LIST"
        const val CLEAR_NOTIFICATIONS_REMINDER_ENABLED = "app.grocery.list.data.settings.CLEAR_NOTIFICATIONS_REMINDER_ENABLED"

        const val OLD_KEY_PRODUCT_TITLE_FORMATTER = $$"app.grocery.list.data.AppRepositoryImpl$productTitleFormatterordinal"
        const val OLD_KEY_BOTTOM_BAR_ROADMAP_STEP = $$"app.grocery.list.data.AppRepositoryImpl$bottomBarRoadmapStepordinal"
        const val OLD_KEY_RECOMMEND_APP_WHEN_SHARING_LIST = $$"app.grocery.list.data.AppRepositoryImpl$recommendAppWhenSharingList"
        const val OLD_KEY_CLEAR_NOTIFICATIONS_REMINDER_ENABLED = $$"app.grocery.list.data.AppRepositoryImpl$clearNotificationsReminderEnabled"
    }
}
