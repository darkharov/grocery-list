package app.grocery.list.domain.question

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.ProductListWasPosted
import app.grocery.list.domain.achievements.ProductWasAddedManually
import app.grocery.list.domain.product.list.CustomProductListsSetting
import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class NeedMoreListsQuestion @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val achievementRepository: AchievementEventRepository,
) : Question() {

    override fun shouldBeAsked(): Flow<Boolean> =
        combine(
            productListRepository.customListsSetting(),
            achievementRepository.happenedTimes(ProductListWasPosted),
            achievementRepository.happenedTimes(ProductWasAddedManually),
        ) {
                customProductListsSetting,
                productListWasPostedTimes,
                productWasAddedManuallyTimes,
            ->
            customProductListsSetting == CustomProductListsSetting.NotSet &&
            productListWasPostedTimes >= 2 &&
            productWasAddedManuallyTimes >= 5
        }

    override suspend fun close() {
        productListRepository.setCustomListsFunctionState(CustomProductListsSetting.Disabled)
    }
}
