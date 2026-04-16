package app.grocery.list.data.product.list.summary.optional.custom.list

import app.grocery.list.data.internal.di.DefaultProductList
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OptionalCustomProductListMapper @Inject constructor(
    @param:DefaultProductList
    private val defaultProductList: ProductList,
) {
    fun toDomain(list: OptionalCustomProductList): ProductList {

        val (id, title, colorScheme) = list

        return if (
            id != null &&
            title != null &&
            colorScheme != null
        ) {
            ProductList(
                id = ProductList.Id.Custom(id),
                title = title,
                colorScheme = ColorScheme.entries[colorScheme],
            )
        } else if (
            id == null &&
            title == null &&
            colorScheme == null
        ) {
            /*
             * The default list is not stored in the database, but its products are.
             * (product.fk_custom_list_id IS NULL in this case)
             *
             * Null values are used in `ProductListWithCountersView` to simulate the default list
             * and count the number of items in each list in a single reusable query.
             */
            defaultProductList
        } else {
            throw IllegalStateException(
                "Wrong state of a product list ($list): " +
                "id, title, colorScheme must be either all NULL or all NON-null"
            )
        }
    }
}
