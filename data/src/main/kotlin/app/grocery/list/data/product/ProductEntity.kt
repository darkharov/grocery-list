package app.grocery.list.data.product

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import app.grocery.list.data.internal.db.SqlAffixes
import app.grocery.list.data.product.emoji.and.keyword.EmojiAndKeywordQuery
import app.grocery.list.data.product.list.custom.CustomProductListEntity

@Entity(
    tableName = ProductEntity.Table.NAME,
    foreignKeys = [
        ForeignKey(
            entity = CustomProductListEntity::class,
            parentColumns = [CustomProductListEntity.Table.Columns.ID],
            childColumns = [ProductEntity.Table.Columns.FK_CUSTOM_LIST_ID],
            onDelete = ForeignKey.CASCADE,
        )
    ],
)
internal class ProductEntity(

    @PrimaryKey
    @ColumnInfo(Table.Columns.ID)
    val id: Int? = null,

    @ColumnInfo(Table.Columns.TITLE)
    val title: String,

    @Embedded
    val emojiAndKeyword: EmojiAndKeywordQuery,

    @ColumnInfo(Table.Columns.ENABLED)
    val enabled: Boolean,

    @ColumnInfo(Table.Columns.NON_FK_CATEGORY_ID)
    val nonFkCategoryId: Int,

    @ColumnInfo(
        Table.Columns.FK_CUSTOM_LIST_ID,
        index = true,
    )
    val customListId: Int?,
) {

    object Table {

        const val NAME = "product"

        object Columns {
            const val ID = NAME + SqlAffixes._ID
            const val TITLE = "title"
            const val ENABLED = "enabled"
            const val NON_FK_CATEGORY_ID = "non_fk_category_id" // categories are not stored in db
            const val FK_CUSTOM_LIST_ID = SqlAffixes.FK_ + "custom_list_id"
        }
    }
}
