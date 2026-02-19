package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.runtime.Stable

@Stable
interface AppToolbarCallbacks {
    fun onUpIconClick()
    fun onAllListsClick()
    fun onSettingsClick()
}

object AppToolbarCallbacksMock : AppToolbarCallbacks {
    override fun onUpIconClick() {}
    override fun onAllListsClick() {}
    override fun onSettingsClick() {}
}
