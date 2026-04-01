package app.grocery.list.domain.question

import app.grocery.list.domain.achievements.AchievementEventRepository
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasDeleted
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasUpdated
import app.grocery.list.domain.achievements.HowToDeleteOrRenameCustomListWasClosed
import app.grocery.list.domain.product.list.ProductListRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class HowToDeleteOrRenameCustomList @Inject internal constructor(
    private val productListRepository: ProductListRepository,
    private val achievementEventRepository: AchievementEventRepository,
) : Question() {

    override fun shouldBeAsked(): Flow<Boolean> =
        combine(
            isQuestionClosed(),
            isUserExperienced(),
            productListRepository.containsAtLeastOneCustomList(),
        ) {
                isQuestionClosed,
                isUserExperienced,
                containsAtLeastOneCustomList,
            ->
            !(isQuestionClosed) &&
            !(isUserExperienced) &&
            containsAtLeastOneCustomList
        }

    private fun isQuestionClosed(): Flow<Boolean> =
        achievementEventRepository.happened(
            HowToDeleteOrRenameCustomListWasClosed,
        )

    private fun isUserExperienced(): Flow<Boolean> =
        achievementEventRepository.happened(
            AtLeastOneCustomProductListWasDeleted,
            AtLeastOneCustomProductListWasUpdated,
        )

    override suspend fun close() {
        achievementEventRepository.put(HowToDeleteOrRenameCustomListWasClosed)
    }
}
