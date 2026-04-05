package app.grocery.list.commons.compose.elements.question

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.values.StringValue

@ConsistentCopyVisibility
@Immutable
data class AppQuestionProps internal constructor(
    val key: String,
    val text: StringValue,
    val payload: Any? = null,
)

internal class AppQuestionMocks : PreviewParameterProvider<AppQuestionProps> {
    override val values = sequenceOf(
        AppQuestionProps(
            key = "key",
            text = StringValue.StringWrapper("Question Text"),
        ),
    )
}
