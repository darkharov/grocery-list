package app.grocery.list.data.product.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CustomProductListDao {

    @Query("SELECT * FROM `custom_product_list`")
    fun all(): Flow<List<CustomProductListEntity>>

    @Insert
    fun insert(entity: CustomProductListEntity)
}
