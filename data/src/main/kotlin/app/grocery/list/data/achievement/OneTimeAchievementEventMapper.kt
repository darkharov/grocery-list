package app.grocery.list.data.achievement

import app.grocery.list.domain.achievements.AchievementEvent
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
            else -> throw UnsupportedOperationException("Unknown event: $event")
        }

    @JvmInline
    value class Result(val mask: Int)
}
