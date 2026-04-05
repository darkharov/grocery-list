package app.grocery.list.domain.question

import app.grocery.list.domain.achievements.AtLeastOneProductWasDeleted
import app.grocery.list.domain.achievements.AtLeastOneProductWasUpdated
import app.grocery.list.domain.achievements.HowToDeleteOrRenameProductWasClosed
import app.grocery.list.domain.achievements.ProductWasAddedManually
import app.grocery.list.domain.product.AtLeastOneProductInCurrentListUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class HowToEditProductsQuestion @Inject constructor(
    private val atLeastOneProductInCurrentList: AtLeastOneProductInCurrentListUseCase,
) : ExperienceBasedQuestion() {

    override val questionClosedEvent = HowToDeleteOrRenameProductWasClosed

    override val experienceCriteria =
        listOf(
            AtLeastOneProductWasDeleted,
            AtLeastOneProductWasUpdated,
        )

    override fun additionalRequirements(): Flow<Boolean> =
        combine(
            atLeastOneProductInCurrentList.execute(enabledOnly = false),
            achievementEventRepository.happenedTimes(ProductWasAddedManually)
        ) {
                atLeastOneProductInCurrentList,
                productWasAddedManuallyTimes,
            ->
            atLeastOneProductInCurrentList &&
            productWasAddedManuallyTimes >= 3
        }
}
