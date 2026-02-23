package app.grocery.list.domain.preview

import app.grocery.list.domain.achievements.AchievementRepository
import app.grocery.list.domain.product.list.CustomListsFeatureState
import app.grocery.list.domain.product.list.CustomListsSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@Singleton
internal class ShouldShowNeedMoreListsQuestionUseCase @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val customListsSettingsRepository: CustomListsSettingsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<Boolean> =
        customListsSettingsRepository
            .featureState()
            .flatMapLatest { state ->
                if (state == CustomListsFeatureState.NotSet) {
                    combine(
                        achievementRepository.productListPostedAtLeastTwice(),
                        achievementRepository.atLeastFiveProductsWereAddedManually(),
                        Boolean::and,
                    )
                } else {
                    flowOf(false)
                }
            }
}
