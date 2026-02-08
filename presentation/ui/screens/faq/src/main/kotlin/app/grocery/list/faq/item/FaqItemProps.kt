package app.grocery.list.faq.item

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal data class FaqItemProps(
    val id: Int,
    val expanded: Boolean,
    val question: String,
    val answer: String,
)

internal class FaqItemMocks : PreviewParameterProvider<FaqItemProps> {

    override val values = sequenceOf(
        FaqItemProps(
            id = 1,
            expanded = false,
            question = "First question",
            answer = "Answer on the first question",
        ),
        FaqItemProps(
            id = 2,
            expanded = true,
            question = "Second question",
            answer = "Answer on the second question",
        ),
    )
}
