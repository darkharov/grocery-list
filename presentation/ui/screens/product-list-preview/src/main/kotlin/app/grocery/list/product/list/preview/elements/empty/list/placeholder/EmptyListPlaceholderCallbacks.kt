package app.grocery.list.product.list.preview.elements.empty.list.placeholder

import androidx.compose.runtime.Stable

@Stable
internal interface EmptyListPlaceholderCallbacks {
    fun onTemplateClick(template: EmptyListPlaceholderProps.Template)
}

internal object EmptyListPlaceholderCallbacksMock : EmptyListPlaceholderCallbacks {
    override fun onTemplateClick(template: EmptyListPlaceholderProps.Template) {}
}
