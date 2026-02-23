package app.grocery.list.domain.notification

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AchievementRepository
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandleProductListPostedUseCase @Inject internal constructor(
    private val settingsRepository: SettingsRepository,
    private val achievementRepository: AchievementRepository,
) {
    suspend fun execute() {
        settingsRepository
            .bottomBarRoadmapStep
            .edit { currentState ->
                if (currentState == BottomBarRoadmapStep.Initial) {
                    BottomBarRoadmapStep.ProductListPostedAtLeastOnce
                } else {
                    currentState
                }
            }
        achievementRepository.put(AchievementEvent.ProductListPosted)
    }
}
