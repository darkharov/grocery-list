package app.grocery.list.custom.product.lists.input.form

import androidx.compose.runtime.Stable
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps

@Stable
internal interface CustomListInputFormCallbacks {
    fun onColorSchemeSelectionChange(newValue: AppColorSchemeProps)
    fun onComplete(title: String, colorScheme: AppColorSchemeProps)
}

internal object CustomListInputFormCallbacksMock : CustomListInputFormCallbacks {
    override fun onColorSchemeSelectionChange(newValue: AppColorSchemeProps) {}
    override fun onComplete(title: String, colorScheme: AppColorSchemeProps) {}
}
