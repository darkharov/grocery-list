package app.grocery.list.domain.achievements

sealed interface AchievementEvent {
    sealed interface OneTime : AchievementEvent
    sealed interface Counting : AchievementEvent
}

data object ProductListWasPosted : AchievementEvent.Counting
data object ProductWasAddedManually : AchievementEvent.Counting
