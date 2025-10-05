package app.grocery.list.settings.list.format

import androidx.compose.runtime.Stable

@Stable
internal interface ListFormatSettingsCallbacks {
    fun onProductTitleFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat)
}

internal object ListFormatSettingsCallbacksMock : ListFormatSettingsCallbacks {
    override fun onProductTitleFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat) {}
}
