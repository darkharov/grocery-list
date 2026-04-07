package app.grocery.list.data.product.list

import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OptionalCustomListMapper @Inject constructor() {

    fun toDomain(query: OptionalCustomListQuery) =
        if (
            query.id == null ||
            query.title == null ||
            query.colorScheme == null
        ) {
            null
        } else {
            ProductList(
                id = ProductList.Id.Custom(backingId = query.id),
                title = query.title,
                colorScheme = ColorScheme.entries[query.colorScheme],
            )
        }
}
