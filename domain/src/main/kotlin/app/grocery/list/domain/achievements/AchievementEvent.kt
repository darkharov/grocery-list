package app.grocery.list.domain.achievements

sealed class AchievementEvent {
    data object ProductListPosted : AchievementEvent()
    data object ProductAddedManually : AchievementEvent()
}
