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

    @Query("SELECT * FROM product_list_and_counters")
    fun listsAndCounters(): Flow<Map<OptionalCustomListQuery, ProductListCountersQuery>>

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

    @Query(
        """
            SELECT COUNT(*) >= 1
              FROM custom_product_list
             LIMIT 1
        """
    )
    fun containsAtLeastOneCustomList(): Flow<Boolean>
}
