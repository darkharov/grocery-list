package app.grocery.list.product.input.form.elements.category.picker

import app.grocery.list.domain.category.Category
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class CategoryMapper @Inject constructor() {

    fun transformList(categories: List<Category>): ImmutableList<CategoryProps> =
        categories.map(::transform).toImmutableList()

    fun transformNullable(category: Category?): CategoryProps? =
        if (category != null) {
            transform(category)
        } else {
            null
        }

    fun transform(category: Category): CategoryProps =
        CategoryProps(
            id = category.id,
            title = category.title,
        )
}
