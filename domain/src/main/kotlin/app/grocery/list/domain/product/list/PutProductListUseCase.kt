package app.grocery.list.domain.product.list

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasUpdated
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutProductListUseCase @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val achievementEventRepository: AchievementEventRepository,
) {
    suspend fun execute(params: ProductList.PutParams) {
        productListRepository.put(params)
        if (params.customListId != null) {
            achievementEventRepository.put(AtLeastOneCustomProductListWasUpdated)
        }
    }
}
