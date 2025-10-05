package app.grocery.list.settings

import androidx.compose.runtime.Stable

@Stable
internal interface SettingsCallbacks {
    fun onListFormatClick()
    fun onContactSupportClick()
}

internal object SettingsCallbacksMock : SettingsCallbacks {
    override fun onListFormatClick() {}
    override fun onContactSupportClick() {}
}
