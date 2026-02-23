package app.grocery.list.domain.preview

import app.grocery.list.domain.achievements.AchievementRepository
import app.grocery.list.domain.product.list.CustomProductListsFeatureSetting
import app.grocery.list.domain.product.list.ProductListRepository
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
    private val productListRepository: ProductListRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun execute(): Flow<Boolean> =
        productListRepository
            .customListsFeatureSetting()
            .flatMapLatest { state ->
                if (state == CustomProductListsFeatureSetting.NotSet) {
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
