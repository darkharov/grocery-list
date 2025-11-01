package app.grocery.list.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.ProductEntity

@Database(
    version = 4,
    entities = [
        ProductEntity::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
