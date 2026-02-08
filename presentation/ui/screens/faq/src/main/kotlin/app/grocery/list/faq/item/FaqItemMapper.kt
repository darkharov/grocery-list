package app.grocery.list.faq.item

import app.grocery.list.domain.faq.FaqItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class FaqItemMapper @AssistedInject constructor(
    @Assisted
    private val selectedItemId: Int?,
) {
    fun toPresentation(item: FaqItem): FaqItemProps =
        FaqItemProps(
            id = item.id,
            expanded = (item.id == selectedItemId),
            question = item.question,
            answer = item.answer,
        )

    fun toPresentationList(item: List<FaqItem>): ImmutableList<FaqItemProps> =
        item.map(::toPresentation).toImmutableList()

    @AssistedFactory
    fun interface Factory {
        fun create(selectedItemId: Int?): FaqItemMapper
    }
}
