package app.grocery.list.data.internal.di.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object MigrationFrom4To5 : Migration(4, 5) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE `custom_product_list` (`custom_product_list_id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL)")
        db.execSQL("CREATE UNIQUE INDEX `index_custom_product_list_title` ON `custom_product_list` (`title`)")
    }
}
