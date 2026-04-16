package app.grocery.list.data.product.list.summary.content.stub

import androidx.room.ColumnInfo
import androidx.room.Embedded
import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordQuery

internal data class ProductListContentStubQuery(

    @ColumnInfo("title")
    val title: String,

    @Embedded
    val emojiAndKeyword: EmojiAndKeywordQuery,
)
