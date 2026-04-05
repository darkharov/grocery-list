package app.grocery.list.domain.product

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.AtLeastOneProductWasDeleted
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val achievementEventRepository: AchievementEventRepository,
) {
    suspend fun execute(productId: Int): Product {
        val deleted = productRepository.delete(productId = productId)
        achievementEventRepository.put(AtLeastOneProductWasDeleted)
        return deleted
    }
}
