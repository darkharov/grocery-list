package app.grocery.list.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertOrReplace(entity: ProductEntity)

    @Insert(onConflict = ABORT)
    suspend fun insertOrReplace(entities: List<ProductEntity>)

    @Query("DELETE FROM product WHERE product_id = :productId")
    suspend fun delete(productId: Int)

    @Transaction
    suspend fun selectAndDelete(productId: Int): ProductEntity {
        val entity = select(productId = productId)
        delete(productId = productId)
        return entity
    }

    @Query("DELETE FROM product")
    suspend fun deleteAll()

    @Query(
        """
            SELECT product.*
              FROM product
             WHERE :enabledOnly == 0
                OR enabled == 1
          ORDER BY non_fk_category_id,
                   title
        """
    )
    fun select(enabledOnly: Boolean): Flow<Map<@MapColumn("non_fk_category_id") Int, List<ProductEntity>>>

    @Query("SELECT * FROM product WHERE product_id == :productId")
    fun select(productId: Int): ProductEntity

    @Query(
        """
            SELECT title,
                   non_fk_category_id
              FROM product
             WHERE product_id == :productId
        """
    )
    fun selectTitleAndCategoryId(productId: Int): ProductEntity.TitleAndCategoryId

    @Query("SELECT * FROM product")
    fun selectProducts(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM product")
    fun count(): Flow<Int>

    @Query("SELECT COUNT(*) FROM product WHERE enabled == 1")
    fun countOfEnabled(): Flow<Int>

    @Query(
        """
            UPDATE product
               SET enabled = :enabled
             WHERE product_id == :productId
        """
    )
    fun setProductEnabled(productId: Int, enabled: Boolean)

    @Query(
        """
            SELECT COUNT(*) > 0
              FROM product
             WHERE enabled == 1
             LIMIT 1
        """
    )
    fun atLeastOneProductEnabled(): Flow<Boolean>

    @Query("UPDATE product SET enabled = :enabled")
    suspend fun setEnabledFlagForAll(enabled: Boolean)
}
