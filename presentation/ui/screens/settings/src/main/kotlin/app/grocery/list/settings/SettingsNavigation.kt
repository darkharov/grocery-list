package app.grocery.list.settings

interface SettingsNavigation {
    fun goToListFormatSettings()
    fun goToBottomBarSettings()
}

object SettingsNavigationMock : SettingsNavigation {
    override fun goToListFormatSettings() {}
    override fun goToBottomBarSettings() {}
}
