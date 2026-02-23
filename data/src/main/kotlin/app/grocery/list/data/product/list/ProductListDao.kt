package app.grocery.list.data.product.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductListDao {

    @Insert
    fun insert(entity: CustomProductListEntity)

    @Query(
        """
             SELECT custom_product_list.*,
                    COUNT(product_id) AS size
               FROM custom_product_list
          LEFT JOIN product
                 ON fk_custom_list_id = custom_product_list_id
           GROUP BY custom_product_list_id
        """
    )
    fun customListsAndSizes(): Flow<List<CustomListAndSizeQuery>>

    @Query(
        """
             SELECT COUNT(*)
               FROM product
              WHERE fk_custom_list_id IS NULL
        """
    )
    fun defaultListSize(): Flow<Int>

    @Query(
        """
             SELECT *
               FROM (
                 SELECT title,
                        keyword,
                        emoji,
                        fk_custom_list_id,
                        ROW_NUMBER() OVER (PARTITION BY fk_custom_list_id ORDER BY title) AS product_number
                   FROM product
                    )
              WHERE product_number <= 3
        """
    )
    fun stubs(): Flow<Map<@MapColumn("fk_custom_list_id") Int?, List<ProductListStubQuery>>>
}
