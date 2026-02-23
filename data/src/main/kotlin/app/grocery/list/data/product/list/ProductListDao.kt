package app.grocery.list.data.product.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductListDao {

    @Insert
    fun insert(entity: CustomProductListEntity)

    @Update
    fun update(entity: CustomProductListEntity)

    @Query("SELECT * FROM custom_product_list WHERE custom_product_list_id = :id")
    fun select(id: Int): Flow<CustomProductListEntity?>

    @Query("DELETE FROM custom_product_list WHERE custom_product_list_id = :id")
    fun delete(id: Int)

    @Query("SELECT title FROM custom_product_list WHERE custom_product_list_id = :id")
    fun selectTitleOfCustomList(id: Int): Flow<String?>

    @Query("SELECT * FROM custom_product_list")
    fun selectAll(): Flow<List<CustomProductListEntity>>

    @Query(
        """
             SELECT custom_product_list.*,
                    COUNT(product_id) AS total_size,
                    COUNT(IIF(enabled, 1, NULL)) AS number_of_enabled_items
               FROM custom_product_list
          LEFT JOIN product
                 ON fk_custom_list_id = custom_product_list_id
           GROUP BY custom_product_list_id
        """
    )
    fun customListsAndCounters(): Flow<List<CustomListAndCountersQuery>>

    @Query(
        """
             SELECT COUNT(product_id) AS total_size,
                    COUNT(IIF(enabled, 1, NULL)) AS number_of_enabled_items
               FROM product
              WHERE fk_custom_list_id IS NULL
        """
    )
    fun defaultListCounters(): Flow<ProductListCountersQuery>

    @Query(
        """
             SELECT *
               FROM (
                 SELECT title,
                        keyword,
                        emoji,
                        fk_custom_list_id,
                        ROW_NUMBER() OVER (
                            PARTITION BY fk_custom_list_id
                            ORDER BY enabled DESC, non_fk_category_id, title
                        ) AS product_number
                   FROM product
                    )
              WHERE product_number <= 3
        """
    )
    fun stubs(): Flow<Map<@MapColumn("fk_custom_list_id") Int?, List<ProductListStubQuery>>>
}
