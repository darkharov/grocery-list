package app.grocery.list.data.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.ProductEntity
import app.grocery.list.data.product.list.CustomProductListEntity
import app.grocery.list.data.product.list.ProductListAndCountersView
import app.grocery.list.data.product.list.ProductListDao

@Database(
    version = 6,
    entities = [
        ProductEntity::class,
        CustomProductListEntity::class,
    ],
    views = [
        ProductListAndCountersView::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productListDao(): ProductListDao
}
