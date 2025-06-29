package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
import kotlinx.collections.immutable.ImmutableList

@Immutable
internal data class ProductInputFormProps(
    val title: String,
    val atLeastOneProductAdded: Boolean,
    val categories: ImmutableList<CategoryProps>,
    val selectedCategory: CategoryProps?,
)

internal object ProductInputFormMocks {

    private val categoryIds = generateSequence(1) { it + 1 }.iterator()

    private val fruitsAndVegetables = CategoryProps(
        id = categoryIds.next(),
        title = "Fruits & Vegetables",
    )

    private val dairyProducts = CategoryProps(
        id = categoryIds.next(),
        title = "Dairy Products",
    )

    val categories = listOf(fruitsAndVegetables, dairyProducts)
}
