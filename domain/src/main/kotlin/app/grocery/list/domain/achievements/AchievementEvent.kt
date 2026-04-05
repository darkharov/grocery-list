package app.grocery.list.domain.achievements

sealed interface AchievementEvent {
    sealed interface OneTime : AchievementEvent
    sealed interface Counting : AchievementEvent
}

data object ProductListWasPosted : AchievementEvent.Counting
data object ProductWasAddedManually : AchievementEvent.Counting

data object AtLeastOneProductWasDeleted : AchievementEvent.OneTime
data object AtLeastOneProductWasUpdated : AchievementEvent.OneTime
data object HowToDeleteOrRenameProductWasClosed : AchievementEvent.OneTime

data object AtLeastOneCustomProductListWasDeleted : AchievementEvent.OneTime
data object AtLeastOneCustomProductListWasUpdated : AchievementEvent.OneTime
data object HowToDeleteOrRenameCustomListWasClosed : AchievementEvent.OneTime
