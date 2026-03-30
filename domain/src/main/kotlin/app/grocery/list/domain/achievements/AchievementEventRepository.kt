package app.grocery.list.domain.achievements

import kotlinx.coroutines.flow.Flow

interface AchievementEventRepository {
    fun happenedTimes(event: AchievementEvent.Counting): Flow<Int>
    fun allHappened(vararg events: AchievementEvent.OneTime): Flow<Boolean>
    suspend fun put(event: AchievementEvent)
}
