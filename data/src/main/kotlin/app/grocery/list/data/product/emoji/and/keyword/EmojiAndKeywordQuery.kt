package app.grocery.list.data.product.emoji.and.keyword

import androidx.room.ColumnInfo

internal data class EmojiAndKeywordQuery(

    @ColumnInfo("emoji")
    val emoji: String?,

    @ColumnInfo("keyword")
    val keyword: String?,
)
