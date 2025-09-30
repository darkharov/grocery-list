package app.grocery.list.settings.use.icons.on.bottom.bar.switch_

import androidx.compose.runtime.Stable

@Stable
internal interface UseIconsOnBottomBarSwitchCallbacks {
    fun onClose()
    fun onUseIconsOnBottomBarCheckedChange(newValue: Boolean)
}

internal object UseIconsOnBottomBarSwitchCallbacksMock : UseIconsOnBottomBarSwitchCallbacks {
    override fun onClose() {}
    override fun onUseIconsOnBottomBarCheckedChange(newValue: Boolean) {}
}
