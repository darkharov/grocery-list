package app.grocery.list.domain.achievements

import kotlinx.coroutines.flow.Flow

interface AchievementEventRepository {
    fun happenedTimes(event: AchievementEvent.Counting): Flow<Int>
    fun happened(vararg events: AchievementEvent.OneTime): Flow<Boolean>
    suspend fun put(event: AchievementEvent)
}
