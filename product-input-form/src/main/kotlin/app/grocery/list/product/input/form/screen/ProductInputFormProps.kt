package app.grocery.list.product.input.form.screen

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class ProductInputFormProps(
    val title: TextFieldValue,
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
