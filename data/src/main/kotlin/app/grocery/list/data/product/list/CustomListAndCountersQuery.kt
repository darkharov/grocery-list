package app.grocery.list.data.product.list

import androidx.room.Embedded

internal class CustomListAndCountersQuery(

    @Embedded
    val customList: CustomProductListEntity,

    @Embedded
    val counters: ProductListCountersQuery,
)
