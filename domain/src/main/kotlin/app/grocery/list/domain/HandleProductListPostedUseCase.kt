package app.grocery.list.domain

import app.grocery.list.storage.value.kotlin.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HandleProductListPostedUseCase @Inject internal constructor(
    private val repository: SettingsRepository,
) {
    suspend fun execute() {
        repository
            .bottomBarRoadmapStep
            .edit { currentState ->
                if (currentState == BottomBarRoadmapStep.Initial) {
                    BottomBarRoadmapStep.ProductListPostedAtLeastOnce
                } else {
                    currentState
                }
            }
    }
}
