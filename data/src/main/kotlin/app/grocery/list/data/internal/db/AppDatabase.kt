package app.grocery.list.data.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.ProductEntity
import app.grocery.list.data.product.list.CustomProductListDao
import app.grocery.list.data.product.list.CustomProductListEntity

@Database(
    version = 5,
    entities = [
        ProductEntity::class,
        CustomProductListEntity::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun customProductListDao(): CustomProductListDao
}
