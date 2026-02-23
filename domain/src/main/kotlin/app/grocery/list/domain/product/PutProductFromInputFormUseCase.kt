package app.grocery.list.domain.product

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AchievementRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutProductFromInputFormUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val achievementRepository: AchievementRepository,
) {
    suspend fun execute(product: Product) {
        productRepository.put(product)
        if (product.id == 0) {
            achievementRepository.put(AchievementEvent.ProductAddedManually)
        }
    }
}
