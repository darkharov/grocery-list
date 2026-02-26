package app.grocery.list.data

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import app.grocery.list.data.settings.SettingsRepositoryImpl
import app.grocery.list.storage.value.android.StorageValueMigrations
import javax.inject.Inject
import kotlinx.coroutines.launch

class InitData @Inject constructor(
    private val storageValueMigrations: StorageValueMigrations,
) {
    fun execute() {
        migrateSettings()
    }

    private fun migrateSettings() {
        ProcessLifecycleOwner
            .get()
            .lifecycleScope
            .launch {
                storageValueMigrations.newKeys(
                    intKeys = listOf(
                        StorageValueMigrations.Keys(
                            old = SettingsRepositoryImpl.OLD_KEY_PRODUCT_TITLE_FORMATTER,
                            new = SettingsRepositoryImpl.PRODUCT_TITLE_FORMATTER,
                        ),
                        StorageValueMigrations.Keys(
                            old = SettingsRepositoryImpl.OLD_KEY_BOTTOM_BAR_ROADMAP_STEP,
                            new = SettingsRepositoryImpl.BOTTOM_BAR_ROADMAP_STEP,
                        ),
                    ),
                    booleanKeys = listOf(
                        StorageValueMigrations.Keys(
                            old = SettingsRepositoryImpl.OLD_KEY_RECOMMEND_APP_WHEN_SHARING_LIST,
                            new = SettingsRepositoryImpl.RECOMMEND_APP_WHEN_SHARING_LIST,
                        ),
                        StorageValueMigrations.Keys(
                            old = SettingsRepositoryImpl.OLD_KEY_CLEAR_NOTIFICATIONS_REMINDER_ENABLED,
                            new = SettingsRepositoryImpl.CLEAR_NOTIFICATIONS_REMINDER_ENABLED,
                        ),
                    )
                )
            }
    }
}