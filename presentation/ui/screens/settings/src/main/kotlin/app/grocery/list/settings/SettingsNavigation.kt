package app.grocery.list.settings

interface SettingsNavigation {
    fun goToListFormatSettings()
    fun goToBottomBarSettings()
    fun goToFaq()
}

object SettingsNavigationMock : SettingsNavigation {
    override fun goToListFormatSettings() {}
    override fun goToBottomBarSettings() {}
    override fun goToFaq() {}
}
