package app.grocery.list.data.settings

import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingsRepositoryImpl @Inject constructor(
    factory: StorageValueFactory,
) : SettingsRepository {

    override val productTitleFormat =
        factory.enum(
            defaultValue = ProductTitleFormat.EmojiAndFullText,
            keyPrefix = "$OLD_KEY_PREFIX\$productTitleFormatter",
        )

    override val clearNotificationsReminderEnabled =
        factory.boolean(
            defaultValue = true,
            key = "$OLD_KEY_PREFIX\$clearNotificationsReminderEnabled",
        )

    override val bottomBarRoadmapStep =
        factory.enum(
            defaultValue = BottomBarRoadmapStep.Initial,
            keyPrefix = "$OLD_KEY_PREFIX\$bottomBarRoadmapStep",
        )

    override val recommendAppWhenSharingList =
        factory.boolean(
            defaultValue = true,
            key = "$OLD_KEY_PREFIX\$recommendAppWhenSharingList",
        )

    companion object {
        const val OLD_KEY_PREFIX = "app.grocery.list.data.AppRepositoryImpl"
    }
}