package app.grocery.list.settings.list.format

internal interface ListFormatSettingsCallbacks {
    fun onProductListFormatSelected(option: ListFormatSettingsProps.ProductItemFormat)
}

internal object ListFormatSettingsCallbacksMock : ListFormatSettingsCallbacks {
    override fun onProductListFormatSelected(option: ListFormatSettingsProps.ProductItemFormat) {}
}
