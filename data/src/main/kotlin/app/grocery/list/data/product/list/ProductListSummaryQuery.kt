package app.grocery.list.data.product.list

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

internal data class ProductListSummaryQuery(

    @Embedded
    val productList: CustomProductListEntity,

    @Relation(
        entityColumn = "product_id",
        parentColumn = "custom_product_list_id",
    )
    val sampleItems: List<ProductListSampleItemView>,

    @ColumnInfo("total_size")
    val totalSize: Int,
)
