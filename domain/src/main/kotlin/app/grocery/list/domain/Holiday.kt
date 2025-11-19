package app.grocery.list.domain

import org.joda.time.LocalDateTime

sealed class Holiday(
    private val emojis: List<String>,
    private val month: Int,
    private val days: IntRange,
) {
    fun emoji(): String? =
        if (timeToChangeEmoji()) {
            emojis.random()
        } else {
            null
        }

    private fun timeToChangeEmoji(): Boolean {
        val now = LocalDateTime.now()
        val currentMonth = now.monthOfYear
        val currentDay = now.dayOfMonth
        return currentMonth == month && days.contains(currentDay)
    }

    companion object {

        private val entries = listOf(
            March8,
            AprilFoolsDay,
            Halloween,
            Christmas,
        )

        fun alternativeEmoji(): String? =
            entries.firstNotNullOfOrNull { it.emoji() }
    }

    private data object Halloween : Holiday(
        emojis = listOf("🎃"),
        month = 10,
        days = (25..31),
    )

    private data object AprilFoolsDay : Holiday(
        emojis = listOf("🤡"),
        month = 4,
        days = (1..1),
    )

    private data object March8 : Holiday(
        emojis = listOf("🌷", "💐"),
        month = 3,
        days = (7..8),
    )

    private data object Christmas : Holiday(
        emojis = listOf("🎄"),
        month = 12,
        days = (20..31),
    )
}
