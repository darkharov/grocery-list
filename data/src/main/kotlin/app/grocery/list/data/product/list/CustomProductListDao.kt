package app.grocery.list.data.product.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CustomProductListDao {

    @Query("SELECT * FROM `custom_product_list`")
    fun all(): Flow<List<CustomProductListEntity>>

    @Query(
        """
            SELECT *
              FROM `custom_product_list`
             WHERE `custom_product_list_id` == :id
        """
    )
    fun select(id: Int): Flow<CustomProductListEntity?>

    @Query(
        """
            SELECT COUNT(*) == 1
              FROM `custom_product_list`
             WHERE `custom_product_list_id` == :id
             LIMIT 1
        """
    )
    fun contains(id: Int): Flow<Boolean>

    @Insert
    fun insert(entity: CustomProductListEntity)
}
