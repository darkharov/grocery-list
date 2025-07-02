package app.grocery.list.assembly.ui.content

import androidx.compose.runtime.Immutable

@Immutable
sealed class AppEvent {

    @Immutable
    data class PushNotificationsGranted(
        val clearNotificationsReminderEnabled: Boolean,
    ) : AppEvent()
}
