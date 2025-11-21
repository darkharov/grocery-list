package app.grocery.list.clear.notifications.reminder

import androidx.compose.runtime.Stable

@Stable
internal interface ClearNotificationsReminderCallbacks {
    fun onDoNotShowAgainCheckedChange(newValue: Boolean)
    fun onNext()
}

internal object ClearNotificationsReminderCallbacksMock : ClearNotificationsReminderCallbacks {
    override fun onDoNotShowAgainCheckedChange(newValue: Boolean) {}
    override fun onNext() {}
}
