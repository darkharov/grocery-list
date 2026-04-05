package app.grocery.list.domain.question

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AchievementEventRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

sealed class ExperienceBasedQuestion : Question() {

    @Inject
    internal lateinit var achievementEventRepository: AchievementEventRepository

    protected abstract val questionClosedEvent: AchievementEvent.OneTime
    protected abstract val experienceCriteria: List<AchievementEvent.OneTime>

    protected open fun additionalRequirements(): Flow<Boolean> =
        flowOf(true)

    final override fun shouldBeAsked(): Flow<Boolean> =
        combine(
            isAlreadyClosed(),
            isUserExperienced(),
            additionalRequirements(),
        ) {
                isAlreadyClosed,
                isUserExperienced,
                additionalRequirements,
            ->
            !(isAlreadyClosed) &&
            !(isUserExperienced) &&
            additionalRequirements
        }

    private fun isAlreadyClosed() =
        achievementEventRepository.happened(questionClosedEvent)

    private fun isUserExperienced() =
        achievementEventRepository.happened(*experienceCriteria.toTypedArray())

    final override suspend fun close() {
        achievementEventRepository.put(questionClosedEvent)
    }
}
