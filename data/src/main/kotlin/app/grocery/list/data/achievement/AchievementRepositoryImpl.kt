package app.grocery.list.data.achievement

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AchievementRepository
import app.grocery.list.storage.value.android.StorageValueFactory
import app.grocery.list.storage.value.kotlin.inc
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class AchievementRepositoryImpl @Inject constructor(
    storageValueFactory: StorageValueFactory,
) : AchievementRepository {

    private val productListPostedTimes = storageValueFactory.int(PRODUCT_LIST_POSTED_TIMES)
    private val productListAddedManuallyTimes = storageValueFactory.int(PRODUCT_ADDED_MANUALLY_TIMES)

    override fun productListPostedAtLeastTwice(): Flow<Boolean> =
        productListPostedTimes.observe().map { it >= 2 }

    override fun atLeastFiveProductsWereAddedManually(): Flow<Boolean> =
        productListAddedManuallyTimes.observe().map { it >= 5 }

    override suspend fun put(event: AchievementEvent) {
        when (event) {
            AchievementEvent.ProductAddedManually -> {
                productListAddedManuallyTimes.inc()
            }
            AchievementEvent.ProductListPosted -> {
                productListPostedTimes.inc()
            }
        }
    }

    private companion object {
        const val PRODUCT_LIST_POSTED_TIMES = "app.grocery.list.data.achievement.PRODUCT_LIST_POSTED_TIMES"
        const val PRODUCT_ADDED_MANUALLY_TIMES = "app.grocery.list.data.achievement.PRODUCT_ADDED_MANUALLY_TIMES"
    }
}
