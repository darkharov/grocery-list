package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import app.grocery.list.data.product.list.CustomProductListEntity.Table

data class OptionalCustomListQuery(

    @ColumnInfo(Table.Columns.ID)
    val id: Int?,

    @ColumnInfo(Table.Columns.TITLE)
    val title: String?,

    @ColumnInfo(Table.Columns.COLOR_SCHEME)
    val colorScheme: Int?,
)
