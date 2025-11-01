package app.grocery.list.data

import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.ProductTitleFormatter
import app.grocery.list.domain.internal.ONLY_FOR_MIGRATION
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.ProductTitleFormat
import app.grocery.list.storage.value.android.StorageValueDelegates
import app.grocery.list.storage.value.kotlin.get
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
internal class AppRepositoryImpl @Inject constructor(
    delegates: StorageValueDelegates,
) : AppRepository {

    @Deprecated(ONLY_FOR_MIGRATION)
    override val productTitleFormat by delegates.enum(defaultValue = ProductTitleFormat.EmojiAndFullText)
    override val productTitleFormatter by delegates.enum(defaultValue = ProductTitleFormatter.EmojiAndFullText)
    override val clearNotificationsReminderEnabled by delegates.boolean(defaultValue = true)
    override val bottomBarRoadmapStep by delegates.enum(defaultValue = BottomBarRoadmapStep.Initial)
    override val recommendAppWhenSharingList by delegates.boolean(defaultValue = true)

    override suspend fun runMigrations() {
        withContext(Dispatchers.IO) {
            migrateProductTitleFormatter()
        }
    }

    @Suppress("DEPRECATION")
    private suspend fun migrateProductTitleFormatter() {
        val oldValue = productTitleFormat.get()
        if (oldValue != ProductTitleFormat.Null) {
            productTitleFormatter.set(ProductTitleFormatter.entries[oldValue.ordinal])
            productTitleFormat.set(ProductTitleFormat.Null)
        }
    }
}
