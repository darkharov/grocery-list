package app.grocery.list.settings

import androidx.compose.runtime.Stable
import app.grocery.list.settings.dialog.SettingsDialogCallbacks
import app.grocery.list.settings.dialog.SettingsDialogCallbacksMock

@Stable
internal interface SettingsCallbacks :
    SettingsDialogCallbacks {
    fun onListFormatClick()
    fun onContactSupportClick()
    fun onFaqClick()
    fun onPrivacyPolicyClick()
    fun onBrowserAppNotFound()
}

internal object SettingsCallbacksMock :
    SettingsCallbacks,
    SettingsDialogCallbacks by SettingsDialogCallbacksMock {
    override fun onListFormatClick() {}
    override fun onContactSupportClick() {}
    override fun onFaqClick() {}
    override fun onPrivacyPolicyClick() {}
    override fun onBrowserAppNotFound() {}
}
