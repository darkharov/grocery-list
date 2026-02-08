package app.grocery.list.faq

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.faq.item.FaqItemMocks
import app.grocery.list.faq.item.FaqItemProps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal class FaqProps(
    val items: ImmutableList<FaqItemProps>,
)

internal class FaqMocks : PreviewParameterProvider<FaqProps> {

    override val values = sequenceOf(
        FaqProps(
            items = FaqItemMocks().values.toImmutableList(),
        ),
    )
}
