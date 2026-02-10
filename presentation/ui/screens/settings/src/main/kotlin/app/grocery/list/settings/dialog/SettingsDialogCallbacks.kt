package app.grocery.list.settings.dialog

import androidx.compose.runtime.Stable

@Stable
internal interface SettingsDialogCallbacks {
    fun onDismiss()
}

internal object SettingsDialogCallbacksMock : SettingsDialogCallbacks {
    override fun onDismiss() {}
}
