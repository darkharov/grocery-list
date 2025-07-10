package app.grocery.list.product.input.form.screen.elements.category.picker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CategoryPickerProps(
    val expanded: Boolean,
    val categories: ImmutableList<CategoryProps>,
    val selectedCategory: CategoryProps?,
)

internal class CategoryPickerMocks : PreviewParameterProvider<CategoryPickerProps> {

    override val values = sequenceOf(prototype)

    companion object {
        val prototype = CategoryPickerProps(
            expanded = true,
            categories = persistentListOf(
                CategoryProps(
                    id = 1,
                    title = "Fruits",
                ),
                CategoryProps(
                    id = 2,
                    title = "Fish",
                ),
            ),
            selectedCategory = null,
        )
    }
}