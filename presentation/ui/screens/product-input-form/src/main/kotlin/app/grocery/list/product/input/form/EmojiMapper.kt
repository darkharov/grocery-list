package app.grocery.list.product.input.form

import app.grocery.list.domain.product.EmojiAndKeyword
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EmojiMapper @Inject constructor() {

    fun toPresentationNullable(emoji: EmojiAndKeyword?): EmojiProps? =
        if (emoji != null) {
            toPresentation(emoji)
        } else {
            null
        }

    fun toPresentation(emoji: EmojiAndKeyword): EmojiProps =
        EmojiProps(
            value = emoji.emoji,
            payload = emoji,
        )

    fun toDomain(emoji: EmojiProps): EmojiAndKeyword =
        emoji.payload as EmojiAndKeyword

    fun toDomainNullable(emoji: EmojiProps?): EmojiAndKeyword? =
        if (emoji != null) {
            toDomain(emoji)
        } else {
            null
        }
}
