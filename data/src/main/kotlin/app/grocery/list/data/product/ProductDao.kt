package app.grocery.list.data.product

import androidx.room.Dao
import androidx.room.Insert
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

    @Query("DELETE FROM product WHERE :customListId IS fk_custom_list_id")
    suspend fun deleteAll(customListId: Int?)

    @Query(
        """
             SELECT *
               FROM product
              WHERE (:customListId IS fk_custom_list_id)
                AND (NOT :enabledOnly OR enabled)
          ORDER BY non_fk_category_id,
                   title
        """
    )
    fun select(
        customListId: Int?,
        enabledOnly: Boolean,
    ): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE product_id == :productId")
    fun select(productId: Int): ProductEntity

    @Query(
        """
             SELECT COUNT(*)
               FROM product
              WHERE (:customListId IS fk_custom_list_id)
                AND (NOT :enabledOnly OR enabled)
        """
    )
    fun count(
        customListId: Int?,
        enabledOnly: Boolean,
    ): Flow<Int>

    @Query("SELECT COUNT(*) FROM product WHERE enabled == 1")
    fun countOfEnabled(): Flow<Int>

    @Query("SELECT COUNT(*) > 0 FROM product WHERE enabled == 1 LIMIT 1")
    fun atLeastOneEnabled(): Flow<Boolean>

    @Query(
        """
            UPDATE product
               SET enabled = :enabled
             WHERE product_id IN (:productIds)
        """
    )
    fun setProductsEnabled(productIds: List<Int>, enabled: Boolean)

    @Query(
        """
            SELECT COUNT(*) > 0
              FROM product
             WHERE (:customListId IS fk_custom_list_id)
               AND (NOT :enabledOnly OR enabled)
             LIMIT 1
        """
    )
    fun any(customListId: Int?, enabledOnly: Boolean): Flow<Boolean>

    @Query(
        """
             UPDATE product
                SET enabled = :enabled
              WHERE :customListId IS fk_custom_list_id
        """
    )
    suspend fun setEnabledFlag(
        enabled: Boolean,
        customListId: Int?,
    )

    @Query("SELECT COUNT(*) > 0 FROM product LIMIT 1")
    fun isThereAtLeastOne(): Flow<Boolean>
}
