package app.grocery.list.data.product.list.summary.counters

import androidx.room.ColumnInfo

internal data class ProductListCountersQuery(

    @ColumnInfo("total_size")
    val totalSize: Int,

    @ColumnInfo("number_of_enabled_items")
    val numberOfEnabledItems: Int,
)
