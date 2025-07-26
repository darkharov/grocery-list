package app.grocery.list.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 3,
    entities = [
        ProductEntity::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
