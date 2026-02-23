package app.grocery.list.domain.achievements

import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun productListPostedAtLeastTwice(): Flow<Boolean>
    fun atLeastFiveProductsWereAddedManually(): Flow<Boolean>
    suspend fun put(event: AchievementEvent)
}
