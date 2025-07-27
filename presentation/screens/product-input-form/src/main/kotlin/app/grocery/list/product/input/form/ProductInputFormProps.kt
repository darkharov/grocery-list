package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps

@Immutable
internal data class ProductInputFormProps(
    val emoji: String?,
    val atLeastOneProductAdded: Boolean,
    val categoryPicker: CategoryPickerProps,
    val payload: Any? = null,
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
