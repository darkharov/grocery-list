package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import androidx.room.Embedded
import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordQuery

internal data class ProductListStubQuery(

    @ColumnInfo("title")
    val title: String,

    @Embedded
    val emojiAndKeyword: EmojiAndKeywordQuery,
)
