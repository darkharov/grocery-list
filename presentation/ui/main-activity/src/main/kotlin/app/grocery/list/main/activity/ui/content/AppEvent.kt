package app.grocery.list.main.activity.ui.content

sealed class AppEvent {

    data class PushNotificationsGranted(
        val clearNotificationsReminderEnabled: Boolean,
    ) : AppEvent()

    data object ActivityResumed : AppEvent()
    data object ScreenLocked : AppEvent()
}
