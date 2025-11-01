package app.grocery.list.domain

import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.internal.ONLY_FOR_MIGRATION
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.storage.value.kotlin.StorageValue

interface AppRepository {

    @Deprecated(ONLY_FOR_MIGRATION)
    val productTitleFormat: StorageValue<ProductTitleFormat>
    val productTitleFormatter: StorageValue<ProductTitleFormatter>
    val clearNotificationsReminderEnabled: StorageValue<Boolean>
    val bottomBarRoadmapStep: StorageValue<BottomBarRoadmapStep>
    val recommendAppWhenSharingList: StorageValue<Boolean>

    suspend fun runMigrations()
}
