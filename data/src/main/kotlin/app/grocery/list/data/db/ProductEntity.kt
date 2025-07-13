package app.grocery.list.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import app.grocery.list.domain.Product
import javax.inject.Inject
import javax.inject.Singleton

@Entity(
    tableName = ProductEntity.Table.NAME,
)
class ProductEntity(

    @PrimaryKey
    @ColumnInfo(Table.Columns.ID)
    val id: Int? = null,

    @ColumnInfo(Table.Columns.TITLE)
    val title: String,

    @ColumnInfo(Table.Columns.EMOJI)
    val emoji: String?,

    @ColumnInfo(Table.Columns.NON_FK_CATEGORY_ID)
    val nonFkCategoryId: Int,
) {

    object Table {

        const val NAME = "product"

        object Columns {
            const val ID = NAME + SqlAffixes._ID
            const val TITLE = "title"
            const val EMOJI = "emoji"
            const val NON_FK_CATEGORY_ID = "non_fk_category_id" // categories are not stored in db
        }
    }

    @Singleton
    class Mapper @Inject constructor() {

        fun toDataEntity(product: Product): ProductEntity =
            ProductEntity(
                id = product.id.takeIf { it != 0 },
                title = product.title,
                emoji = product.emoji,
                nonFkCategoryId = product.categoryId,
            )

        fun toDataEntities(products: List<Product>): List<ProductEntity> =
            products.map { toDataEntity(it) }

        fun toDomainModel(entity: ProductEntity) =
            Product(
                id = entity.id ?: throw IllegalStateException("ProductEntity must be queried from DB"),
                title = entity.title,
                emoji = entity.emoji,
                categoryId = entity.nonFkCategoryId,
            )

        fun toDomainModels(entities: List<ProductEntity>) =
            entities.map(::toDomainModel)
    }
}
