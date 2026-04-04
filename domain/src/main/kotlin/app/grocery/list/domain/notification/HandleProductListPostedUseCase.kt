package app.grocery.list.domain.notification

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.ProductListWasPosted
import app.grocery.list.domain.settings.BottomBarSetting
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
            .bottomBarSetting
            .edit { currentState ->
                if (currentState == BottomBarSetting.Initial) {
                    BottomBarSetting.TimeToSuggestSwitchingToIcons
                } else {
                    currentState
                }
            }
        achievementRepository.put(ProductListWasPosted)
    }
}
