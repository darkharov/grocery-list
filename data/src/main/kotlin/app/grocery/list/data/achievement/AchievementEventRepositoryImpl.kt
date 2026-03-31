package app.grocery.list.data.achievement

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.ProductListWasPosted
import app.grocery.list.domain.achievements.ProductWasAddedManually
import app.grocery.list.storage.value.android.StorageValueFactory
import app.grocery.list.storage.value.kotlin.containsFlags
import app.grocery.list.storage.value.kotlin.enableFlags
import app.grocery.list.storage.value.kotlin.inc
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
internal class AchievementEventRepositoryImpl @Inject constructor(
    storageValueFactory: StorageValueFactory,
    private val oneTimeAchievementEventMapper: OneTimeAchievementEventMapper,
) : AchievementEventRepository {

    private val productListPostedTimes = storageValueFactory.int(PRODUCT_LIST_POSTED_TIMES)
    private val productListAddedManuallyTimes = storageValueFactory.int(PRODUCT_ADDED_MANUALLY_TIMES)
    private val oneTimeAchievementEventFlags = storageValueFactory.int(ONE_TIME_ACHIEVEMENT_EVENT_FLAGS)

    override fun happenedTimes(event: AchievementEvent.Counting): Flow<Int> =
        when (event) {
            ProductListWasPosted -> {
                productListPostedTimes.observe()
            }
            ProductWasAddedManually -> {
                productListAddedManuallyTimes.observe()
            }
        }

    override fun happened(vararg events: AchievementEvent.OneTime): Flow<Boolean> {
        val result = oneTimeAchievementEventMapper.toData(events)
        return oneTimeAchievementEventFlags.containsFlags(result.mask)
    }

    override suspend fun put(event: AchievementEvent) {
        when (event) {
            is AchievementEvent.OneTime -> {
                putOneTimeEvent(event)
            }
            is AchievementEvent.Counting -> {
                putCountingEvent(event)
            }
        }
    }

    private suspend fun putOneTimeEvent(event: AchievementEvent.OneTime) {
        val result = oneTimeAchievementEventMapper.toData(arrayOf(event))
        oneTimeAchievementEventFlags.enableFlags(result.mask)
    }

    private suspend fun putCountingEvent(event: AchievementEvent.Counting) {
        when (event) {
            is ProductWasAddedManually -> {
                productListAddedManuallyTimes.inc()
            }
            is ProductListWasPosted -> {
                productListPostedTimes.inc()
            }
        }
    }

    private companion object {
        const val PRODUCT_LIST_POSTED_TIMES = "app.grocery.list.data.achievement.PRODUCT_LIST_POSTED_TIMES"
        const val PRODUCT_ADDED_MANUALLY_TIMES = "app.grocery.list.data.achievement.PRODUCT_ADDED_MANUALLY_TIMES"
        const val ONE_TIME_ACHIEVEMENT_EVENT_FLAGS = "app.grocery.list.data.achievement.ONE_TIME_ACHIEVEMENT_EVENT_FLAGS"
    }
}
