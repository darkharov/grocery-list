package app.grocery.list.data.internal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import app.grocery.list.data.product.ProductDao
import app.grocery.list.data.product.ProductEntity
import app.grocery.list.data.product.list.ProductListDao
import app.grocery.list.data.product.list.custom.CustomProductListEntity
import app.grocery.list.data.product.list.summary.ProductListWithCountersView

@Database(
    version = 6,
    entities = [
        ProductEntity::class,
        CustomProductListEntity::class,
    ],
    views = [
        ProductListWithCountersView::class,
    ],
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun productListDao(): ProductListDao
}
