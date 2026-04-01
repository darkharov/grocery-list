package app.grocery.list.domain.product.list

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasDeleted
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCustomProductListUseCase @Inject constructor(
    private val productListRepository: ProductListRepository,
    private val achievementEventRepository: AchievementEventRepository,
) {
    suspend fun execute(id: ProductList.Id.Custom) {
        productListRepository.delete(id)
        achievementEventRepository.put(AtLeastOneCustomProductListWasDeleted)
    }
}
