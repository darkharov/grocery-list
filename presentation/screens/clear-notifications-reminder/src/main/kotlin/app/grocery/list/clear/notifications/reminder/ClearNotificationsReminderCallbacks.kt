package app.grocery.list.clear.notifications.reminder

internal interface ClearNotificationsReminderCallbacks {
    fun onDoNotShowAgainCheckedChange(newValue: Boolean)
    fun onNext()
}

internal object ClearNotificationsReminderCallbacksMock : ClearNotificationsReminderCallbacks {
    override fun onDoNotShowAgainCheckedChange(newValue: Boolean) {}
    override fun onNext() {}
}
