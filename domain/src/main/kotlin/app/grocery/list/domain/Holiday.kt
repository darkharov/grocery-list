package app.grocery.list.domain

import org.joda.time.LocalDateTime

sealed interface Holiday {

    fun timeToChangeEmoji(): Boolean
    fun emoji(): String

    companion object {
        private val entries = listOf(
            March8,
            AprilFoolsDay,
            Halloween,
            Christmas,
        )

        fun alternativeEmoji(): String? =
            entries
                .find { it.timeToChangeEmoji() }
                ?.emoji()
    }

    private data object Halloween :
        Holiday by SimpleHoliday(
            emojis = listOf("🎃"),
            month = 10,
            days = (25..31),
        )

    private data object AprilFoolsDay :
        Holiday by SimpleHoliday(
            emojis = listOf("🤡"),
            month = 4,
            days = (1..1),
        )

    private data object March8 :
        Holiday by SimpleHoliday(
            emojis = listOf("🌷", "💐"),
            month = 3,
            days = (7..8),
        )

    private data object Christmas :
        Holiday by SimpleHoliday(
            emojis = listOf("🎄"),
            month = 12,
            days = (20..31),
        )

    private class SimpleHoliday(
        private val emojis: List<String>,
        private val month: Int,
        private val days: IntRange? = null,
    ) : Holiday {

        override fun timeToChangeEmoji(): Boolean {
            val now = LocalDateTime.now()
            val currentMonth = now.monthOfYear
            val currentDay = now.dayOfMonth
            return currentMonth == month && (days?.contains(currentDay) ?: true)
        }

        override fun emoji(): String =
            emojis.random()
    }
}
