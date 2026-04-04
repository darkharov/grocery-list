package app.grocery.list.settings.child.screens.bottom.bar.settings

import androidx.compose.runtime.Stable

@Stable
internal interface BottomBarSettingsCallbacks {
    fun onCheckedChange(newValue: Boolean)
}

internal object BottomBarSettingsCallbacksMock : BottomBarSettingsCallbacks {
    override fun onCheckedChange(newValue: Boolean) {}
}
