package app.grocery.list.data.product.list

import androidx.room.ColumnInfo

internal class ProductListCountersQuery(

    @ColumnInfo("total_size")
    val totalSize: Int,

    @ColumnInfo("number_of_enabled_items")
    val numberOfEnabledItems: Int,
)
