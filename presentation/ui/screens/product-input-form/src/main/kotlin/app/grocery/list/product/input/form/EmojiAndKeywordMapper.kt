package app.grocery.list.product.input.form

import app.grocery.list.domain.product.EmojiAndKeyword
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EmojiAndKeywordMapper @Inject constructor() {

    fun transformNullable(emoji: EmojiAndKeyword?): EmojiProps? =
        if (emoji == null) {
            null
        } else {
            EmojiProps(
                value = emoji.emoji,
                payload = emoji,
            )
        }
}
