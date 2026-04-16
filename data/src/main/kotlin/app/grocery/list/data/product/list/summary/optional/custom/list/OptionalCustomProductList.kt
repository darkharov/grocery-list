package app.grocery.list.data.product.list.summary.optional.custom.list

import androidx.room.ColumnInfo

data class OptionalCustomProductList(

    @ColumnInfo("custom_product_list_id")
    val id: Int?,

    @ColumnInfo("custom_product_list_title")
    val title: String?,

    @ColumnInfo("custom_product_list_color_scheme")
    val colorScheme: Int?,
)
