package app.grocery.list.product.list.preview.elements.empty.list.placeholder

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.values.StringValue

@Immutable
data class EmptyListPlaceholderProps(
    val key: String,
    val text: StringValue,
    val templates: List<Template>?,
) {
    @Immutable
    data class Template(
        val id: Int,
        val title: String,
    )
}

internal class EmptyListPlaceholderMocks : PreviewParameterProvider<EmptyListPlaceholderProps> {

    override val values = sequenceOf(prototype)

    companion object {
        val prototype = EmptyListPlaceholderProps(
            key = "EmptyListPlaceholderProps",
            text = StringValue.StringWrapper("Text"),
            templates = listOf(
                EmptyListPlaceholderProps.Template(
                    id = 1,
                    title = "Template1",
                ),
                EmptyListPlaceholderProps.Template(
                    id = 2,
                    title = "Template2",
                ),
            ),
        )
    }
}
