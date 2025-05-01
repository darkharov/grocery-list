package app.grocery.list.product.input.form.screen.elements.title.input

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue

@Stable
internal interface ProductTitleInputCallbacks {
    fun onProductTitleChange(newValue: TextFieldValue)
}

internal object ProductTitleInputCallbacksMock : ProductTitleInputCallbacks {
    override fun onProductTitleChange(newValue: TextFieldValue) {}
}
