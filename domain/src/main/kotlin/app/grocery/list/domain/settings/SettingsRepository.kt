package app.grocery.list.domain.settings

import app.grocery.list.storage.value.kotlin.StorageValue

interface SettingsRepository {
    val productTitleFormat: StorageValue<ProductTitleFormat>
    val clearNotificationsReminderEnabled: StorageValue<Boolean>
    val bottomBarSetting: StorageValue<BottomBarSetting>
    val recommendAppWhenSharingList: StorageValue<Boolean>
}
