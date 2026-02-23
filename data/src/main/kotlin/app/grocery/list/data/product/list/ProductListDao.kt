package app.grocery.list.data.product.list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductListDao {

    @Query(
        """
              SELECT COUNT(*) AS total_size,
                     custom_product_list.*
                FROM product
           LEFT JOIN custom_product_list
                  ON custom_product_list_id = fk_custom_list_id
            GROUP BY fk_custom_list_id
        """
    )
    fun all(): Flow<List<ProductListSummaryQuery>>

    @Insert
    fun insert(entity: CustomProductListEntity)
}
