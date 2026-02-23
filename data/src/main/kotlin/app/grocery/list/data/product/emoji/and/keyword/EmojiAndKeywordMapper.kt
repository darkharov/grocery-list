package app.grocery.list.data.product.emoji.and.keyword

import app.grocery.list.domain.product.EmojiAndKeyword
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EmojiAndKeywordMapper @Inject constructor() {

    fun toDomain(projection: EmojiAndKeywordQuery): EmojiAndKeyword? {

        val emoji = projection.emoji
        val keyword = projection.keyword

        return if (
            !(emoji.isNullOrBlank()) &&
            !(keyword.isNullOrBlank())
        ) {
            EmojiAndKeyword(
                emoji = emoji,
                keyword = keyword
            )
        } else {
            null
        }
    }

    fun toData(emojiAndKeyword: EmojiAndKeyword?): EmojiAndKeywordQuery =
        EmojiAndKeywordQuery(
            emoji = emojiAndKeyword?.emoji,
            keyword = emojiAndKeyword?.keyword,
        )
}
