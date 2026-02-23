package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordProjection

@DatabaseView(
    viewName = "product_list_sample_item",
    value =
        """
             SELECT product_id,
                    title,
                    emoji,
                    keyword,
                    fk_custom_list_id
               FROM product
              LIMIT 3
        """,
)
internal class ProductListSampleItemView(

    @ColumnInfo("product_id")
    val productId: String,

    @ColumnInfo("title")
    val title: String,

    @Embedded
    val emojiAndKeyword: EmojiAndKeywordProjection,

    @ColumnInfo("fk_custom_list_id")
    val fkCustomListId: String,
)
