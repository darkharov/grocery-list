package app.grocery.list.data.product.emoji.and.keyword

import androidx.room.ColumnInfo

internal data class EmojiAndKeywordProjection(

    @ColumnInfo(Columns.EMOJI)
    val emoji: String?,

    @ColumnInfo(Columns.KEYWORD)
    val keyword: String?,
) {
    object Columns {
        const val EMOJI = "emoji"
        const val KEYWORD = "keyword"
    }
}
