package app.grocery.list.settings

import androidx.compose.runtime.Stable

@Stable
internal interface SettingsCallbacks {
    fun onListFormatClick()
    fun onContactSupportClick()
    fun onFaqClick()
}

internal object SettingsCallbacksMock : SettingsCallbacks {
    override fun onListFormatClick() {}
    override fun onContactSupportClick() {}
    override fun onFaqClick() {}
}
