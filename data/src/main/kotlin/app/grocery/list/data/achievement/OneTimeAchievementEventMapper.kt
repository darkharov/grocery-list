package app.grocery.list.data.achievement

import app.grocery.list.domain.achievements.AchievementEvent
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasDeleted
import app.grocery.list.domain.achievements.AtLeastOneCustomProductListWasUpdated
import app.grocery.list.domain.achievements.HowToDeleteOrRenameCustomListWasClosed
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OneTimeAchievementEventMapper @Inject constructor() {

    fun toData(events: Array<out AchievementEvent.OneTime>): Result =
        Result(
            events
                .map(::toIndex)
                .fold(0) { mask, index ->
                    mask or (1 shl index)
                }
        )

    private fun toIndex(event: AchievementEvent.OneTime): Int =
        when (event) {
            AtLeastOneCustomProductListWasDeleted -> {
                AT_LEAST_ONE_CUSTOM_PRODUCT_LIST_WAS_DELETED
            }
            AtLeastOneCustomProductListWasUpdated -> {
                AT_LEAST_ONE_CUSTOM_PRODUCT_LIST_WAS_UPDATED
            }
            HowToDeleteOrRenameCustomListWasClosed -> {
                HOW_TO_DELETE_OR_RENAME_CUSTOM_LIST_WAS_CLOSED
            }
        }

    @JvmInline
    value class Result(val mask: Int)

    companion object {
        const val AT_LEAST_ONE_CUSTOM_PRODUCT_LIST_WAS_DELETED = 0
        const val AT_LEAST_ONE_CUSTOM_PRODUCT_LIST_WAS_UPDATED = 1
        const val HOW_TO_DELETE_OR_RENAME_CUSTOM_LIST_WAS_CLOSED = 2
    }
}
