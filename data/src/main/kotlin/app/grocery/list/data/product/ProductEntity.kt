package app.grocery.list.data.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.grocery.list.data.internal.db.SqlAffixes
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import javax.inject.Inject
import javax.inject.Singleton

@Entity(
    tableName = ProductEntity.Table.NAME,
)
internal class ProductEntity(

    @PrimaryKey
    @ColumnInfo(Table.Columns.ID)
    val id: Int? = null,

    @ColumnInfo(Table.Columns.TITLE)
    val title: String,

    @ColumnInfo(Table.Columns.EMOJI)
    val emoji: String?,

    @ColumnInfo(Table.Columns.KEYWORD)
    val keyword: String?,

    @ColumnInfo(Table.Columns.ENABLED)
    val enabled: Boolean,

    @ColumnInfo(Table.Columns.NON_FK_CATEGORY_ID)
    val nonFkCategoryId: Int,
) {

    object Table {

        const val NAME = "product"

        object Columns {
            const val ID = NAME + SqlAffixes._ID
            const val TITLE = "title"
            const val EMOJI = "emoji"
            const val KEYWORD = "keyword"
            const val ENABLED = "enabled"
            const val NON_FK_CATEGORY_ID = "non_fk_category_id" // categories are not stored in db
        }
    }

    @Singleton
    class Mapper @Inject constructor() {

        fun toDataEntity(product: Product): ProductEntity =
            ProductEntity(
                id = product.id.takeIf { it != 0 },
                title = product.title,
                emoji = product.emojiAndKeyword?.emoji,
                keyword = product.emojiAndKeyword?.keyword,
                enabled = product.enabled,
                nonFkCategoryId = product.categoryId,
            )

        fun toDataEntities(products: List<Product>): List<ProductEntity> =
            products.map { toDataEntity(it) }

        fun toDomainModel(entity: ProductEntity): Product {
            val emoji = entity.emoji
            val keyword = entity.keyword
            return Product(
                id = entity.id ?: throw IllegalStateException("ProductEntity must be queried from DB"),
                title = entity.title,
                emojiAndKeyword = if (
                    !(emoji.isNullOrBlank()) &&
                    !(keyword.isNullOrBlank())
                ) {
                    EmojiAndKeyword(
                        emoji = emoji,
                        keyword = keyword
                    )
                } else {
                    null
                },
                enabled = entity.enabled,
                categoryId = entity.nonFkCategoryId,
            )
        }

        fun toDomainModels(entities: List<ProductEntity>) =
            entities.map(::toDomainModel)
    }
}
