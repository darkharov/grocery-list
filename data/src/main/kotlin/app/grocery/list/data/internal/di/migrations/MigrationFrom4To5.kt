package app.grocery.list.data.internal.di.migrations

import androidx.core.database.getStringOrNull
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.grocery.list.data.product.ProductEntity

internal object MigrationFrom4To5 : Migration(4, 5) {

    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {

            execSQL("CREATE TABLE `custom_product_list` (`custom_product_list_id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `color_scheme` INTEGER NOT NULL DEFAULT 0)")
            execSQL("CREATE UNIQUE INDEX `index_custom_product_list_title` ON `custom_product_list` (`title`)")

            val cursor = query("SELECT * FROM `product`")
            val products = mutableListOf<ProductEntity>()
            while (cursor.moveToNext()) {
                products.add(
                    ProductEntity(
                        id = cursor.getInt(0),
                        title = cursor.getString(1),
                        emoji = cursor.getStringOrNull(2),
                        keyword = cursor.getStringOrNull(3),
                        enabled = cursor.getInt(4) == 1,
                        nonFkCategoryId = cursor.getInt(5),
                        customListId = null,
                    )
                )
            }

            execSQL("DROP TABLE `product`")
            execSQL("CREATE TABLE `product` (`product_id` INTEGER, `title` TEXT NOT NULL, `emoji` TEXT, `keyword` TEXT, `enabled` INTEGER NOT NULL, `non_fk_category_id` INTEGER NOT NULL, `fk_custom_list_id` INTEGER DEFAULT NULL, PRIMARY KEY(`product_id`), FOREIGN KEY(`fk_custom_list_id`) REFERENCES `custom_product_list`(`custom_product_list_id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            execSQL("CREATE INDEX `index_product_fk_custom_list_id` ON `product` (`fk_custom_list_id`)")
            for (product in products) {
                execSQL(
                    """
                        INSERT INTO `product`(`product_id`, `title`, `emoji`, `keyword`, `enabled`, `non_fk_category_id`)
                             VALUES (${product.id}, '${product.title}', '${product.emoji}', '${product.keyword}', ${if (product.enabled) 1 else 0}, ${product.nonFkCategoryId})
                    """.trimIndent()
                )
            }
        }
    }
}
