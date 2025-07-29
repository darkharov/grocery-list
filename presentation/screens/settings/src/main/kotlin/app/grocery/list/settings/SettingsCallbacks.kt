package app.grocery.list.settings

internal interface SettingsCallbacks {
    fun onListFormatClick()
    fun onContactSupportClick()
}

internal object SettingsCallbacksMock : SettingsCallbacks {
    override fun onListFormatClick() {}
    override fun onContactSupportClick() {}
}
