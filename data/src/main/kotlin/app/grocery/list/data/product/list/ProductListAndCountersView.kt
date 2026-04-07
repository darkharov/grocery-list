package app.grocery.list.data.product.list

import androidx.room.DatabaseView

@DatabaseView(
    value = """
             SELECT product_list.custom_product_list_id,
                    product_list.title,
                    product_list.color_scheme,
                    COUNT(product_id) AS total_size,
                    COUNT(IIF(enabled, 1, NULL)) AS number_of_enabled_items
               FROM (
                 SELECT *
                   FROM custom_product_list
                  UNION VALUES (NULL, NULL, NULL)
                    ) AS product_list
          LEFT JOIN product
                 ON fk_custom_list_id IS custom_product_list_id
           GROUP BY custom_product_list_id
    """,
    viewName = "product_list_and_counters",
)
internal class ProductListAndCountersView
