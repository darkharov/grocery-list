package app.grocery.list.domain.question

import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasDeleted
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasUpdated
import app.grocery.list.domain.achievements.HowToDeleteOrRenameCustomListWasClosed
import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class HowToEditCustomListsQuestion @Inject internal constructor(
    private val productListRepository: ProductListRepository,
) : ExperienceBasedQuestion() {

    override val questionClosedEvent = HowToDeleteOrRenameCustomListWasClosed

    override val experienceCriteria =
        listOf(
            AtLeastOneCustomProductListWasDeleted,
            AtLeastOneCustomProductListWasUpdated,
        )

    override fun additionalRequirements(): Flow<Boolean> =
        productListRepository.containsAtLeastOneCustomList()
}
