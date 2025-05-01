package app.grocery.list.product.input.form.mapping

import app.grocery.list.domain.Product
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class ProductCategoryMapper @Inject constructor() {

    fun transformList(categories: List<Product.Category>): ImmutableList<CategoryProps> =
        categories.map(::transform).toImmutableList()

    fun transformNullable(category: Product.Category?): CategoryProps? =
        if (category != null) {
            transform(category)
        } else {
            null
        }

    fun transform(category: Product.Category): CategoryProps =
        CategoryProps(
            id = category.id,
            title = category.title,
        )
}
