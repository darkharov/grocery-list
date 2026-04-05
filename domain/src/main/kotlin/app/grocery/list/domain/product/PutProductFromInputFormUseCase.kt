package app.grocery.list.domain.product

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.AtLeastOneProductWasUpdated
import app.grocery.list.domain.achievements.ProductWasAddedManually
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PutProductFromInputFormUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val achievementEventRepository: AchievementEventRepository,
) {
    suspend fun execute(product: Product) {
        productRepository.put(product)
        if (product.id == 0) {
            achievementEventRepository.put(ProductWasAddedManually)
        } else {
            achievementEventRepository.put(AtLeastOneProductWasUpdated)
        }
    }
}
