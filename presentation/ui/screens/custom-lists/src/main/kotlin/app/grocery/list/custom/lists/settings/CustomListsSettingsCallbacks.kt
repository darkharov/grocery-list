package app.grocery.list.custom.lists.settings

import androidx.compose.runtime.Stable

@Stable
internal interface CustomListsSettingsCallbacks {
    fun onCustomListsEnabledChange(newValue: Boolean)
}

internal object CustomListsSettingsCallbacksMock : CustomListsSettingsCallbacks {
    override fun onCustomListsEnabledChange(newValue: Boolean) {}
}
