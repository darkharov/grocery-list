package app.grocery.list.settings.list.format

internal interface ListFormatSettingsCallbacks {
    fun onProductListFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat)
}

internal object ListFormatSettingsCallbacksMock : ListFormatSettingsCallbacks {
    override fun onProductListFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat) {}
}
