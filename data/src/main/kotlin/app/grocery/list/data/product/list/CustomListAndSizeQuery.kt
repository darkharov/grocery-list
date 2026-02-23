package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import androidx.room.Embedded

internal class CustomListAndSizeQuery(

    @Embedded
    val customList: CustomProductListEntity,

    @ColumnInfo("size")
    val size: Int,
)
