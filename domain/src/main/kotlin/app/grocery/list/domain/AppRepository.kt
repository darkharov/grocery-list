package app.grocery.list.domain

import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.storage.value.kotlin.StorageValue

interface AppRepository {
    val productTitleFormatter: StorageValue<ProductTitleFormatter>
    val clearNotificationsReminderEnabled: StorageValue<Boolean>
    val bottomBarRoadmapStep: StorageValue<BottomBarRoadmapStep>
    val recommendAppWhenSharingList: StorageValue<Boolean>
}
