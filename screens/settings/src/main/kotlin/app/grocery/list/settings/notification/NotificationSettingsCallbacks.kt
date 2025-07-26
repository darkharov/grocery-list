package app.grocery.list.settings.notification

internal interface NotificationSettingsCallbacks {
    fun onItemInNotificationModeSelected(mode: NotificationSettingsProps.ItemInNotificationMode)
}

internal object NotificationSettingsCallbacksMock : NotificationSettingsCallbacks {
    override fun onItemInNotificationModeSelected(mode: NotificationSettingsProps.ItemInNotificationMode) {}
}
