package app.grocery.list.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertOrReplace(entity: ProductEntity)

    @Insert(onConflict = ABORT)
    suspend fun insertOrReplace(entities: List<ProductEntity>)

    @Query("DELETE FROM product WHERE product_id = :productId")
    suspend fun delete(productId: Int)

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    @Query(
        """
   SELECT DISTINCT added_category.non_fk_category_id,
                   product.*
              FROM product
                AS added_category
         LEFT JOIN product
                ON product.non_fk_category_id = added_category.non_fk_category_id
          ORDER BY added_category.non_fk_category_id
        """
    )
    fun select(): Flow<Map<@MapColumn("non_fk_category_id") Int, List<ProductEntity>>>

    @Query("SELECT * FROM product")
    fun selectProducts(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM product")
    fun count(): Flow<Int>

}
