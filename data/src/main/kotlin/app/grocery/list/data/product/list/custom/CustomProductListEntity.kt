package app.grocery.list.data.product.list.custom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.grocery.list.data.internal.db.SqlAffixes

@Entity(
    tableName = CustomProductListEntity.Table.NAME,
)
internal class CustomProductListEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(Table.Columns.ID)
    val id: Int,

    @ColumnInfo(name = Table.Columns.TITLE)
    val title: String,

    @ColumnInfo(
        name = Table.Columns.COLOR_SCHEME,
        defaultValue = "0",
    )
    val colorScheme: Int,
) {

    object Table {

        const val NAME = "custom_product_list"

        object Columns {
            const val ID = NAME + SqlAffixes._ID
            const val TITLE = "title"
            const val COLOR_SCHEME = "color_scheme"
        }
    }
}
