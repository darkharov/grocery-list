package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import app.grocery.list.data.internal.db.SqlAffixes

@Entity(
    tableName = CustomProductListEntity.Table.NAME,
    indices = [
        Index(
            value = [CustomProductListEntity.Table.Columns.TITLE],
            unique = true,
        )
    ]
)
internal class CustomProductListEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(Table.Columns.ID)
    val id: Int?,

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
