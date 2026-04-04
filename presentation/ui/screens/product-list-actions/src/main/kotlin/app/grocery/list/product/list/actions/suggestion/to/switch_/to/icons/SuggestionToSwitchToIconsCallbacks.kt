package app.grocery.list.product.list.actions.suggestion.to.switch_.to.icons

import androidx.compose.runtime.Stable

@Stable
internal interface SuggestionToSwitchToIconsCallbacks {
    fun onRefuseToSwitchToIcons()
    fun onSwitchToIcons()
}

internal object SuggestionToSwitchToIconsCallbacksMock : SuggestionToSwitchToIconsCallbacks {
    override fun onRefuseToSwitchToIcons() {}
    override fun onSwitchToIcons() {}
}
