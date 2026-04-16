package app.grocery.list.data.product.list.summary

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import app.grocery.list.data.product.list.summary.counters.ProductListCountersQuery
import app.grocery.list.data.product.list.summary.optional.custom.list.OptionalCustomProductList

@Suppress("unused")
@DatabaseView(
    value = """
             SELECT product_list.custom_product_list_id AS custom_product_list_id,
                    product_list.title                  AS custom_product_list_title,
                    product_list.color_scheme           AS custom_product_list_color_scheme,
                    COUNT(product_id)                   AS total_size,
                    COUNT(IIF(enabled, 1, NULL))        AS number_of_enabled_items,
                    LAG (custom_product_list_id)
                    OVER (ORDER BY (SELECT NULL))       AS trailing_id,
                    LEAD (custom_product_list_id)
                    OVER (ORDER BY (SELECT NULL))       AS leading_id
               FROM (
                     SELECT *
                       FROM custom_product_list
                      UNION VALUES (NULL, NULL, NULL)       -- To simulate the default list
                    ) AS product_list
          LEFT JOIN product
                 ON fk_custom_list_id IS custom_product_list_id
           GROUP BY custom_product_list_id
    """,
    viewName = "product_list_with_counters",
)
internal data class ProductListWithCountersView(

    @Embedded
    val optionalCustomProductList: OptionalCustomProductList,

    @Embedded
    val counters: ProductListCountersQuery,

    @ColumnInfo("trailing_id")
    val trailingId: Int?,

    @ColumnInfo("leading_id")
    val leadingId: Int?,
) {
    val customListId get() = optionalCustomProductList.id
}
