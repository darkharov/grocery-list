package app.grocery.list.domain.notification

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.ProductListWasPosted
import app.grocery.list.domain.settings.BottomBarRoadmapStep
import app.grocery.list.domain.settings.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandleProductListPostedUseCase @Inject internal constructor(
    private val settingsRepository: SettingsRepository,
    private val achievementRepository: AchievementEventRepository,
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
        achievementRepository.put(ProductListWasPosted)
    }
}
