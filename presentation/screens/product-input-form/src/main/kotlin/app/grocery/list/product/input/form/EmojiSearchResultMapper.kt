package app.grocery.list.product.input.form

import app.grocery.list.domain.EmojiSearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EmojiSearchResultMapper @Inject constructor() {

    fun transformNullable(emoji: EmojiSearchResult?): EmojiProps? =
        if (emoji == null) {
            null
        } else {
            EmojiProps(
                value = emoji.emoji,
                payload = emoji,
            )
        }
}
