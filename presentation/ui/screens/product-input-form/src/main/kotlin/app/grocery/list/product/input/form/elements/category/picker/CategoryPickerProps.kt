package app.grocery.list.product.input.form.elements.category.picker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CategoryPickerProps(
    val items: ImmutableList<CategoryProps> = persistentListOf(),
    val expanded: Boolean = false,
    val selectedOne: CategoryProps? = null,
)

internal class CategoryPickerMocks : PreviewParameterProvider<CategoryPickerProps> {

    override val values = sequenceOf(prototype)

    companion object {
        val prototype = CategoryPickerProps(
            expanded = true,
            items = persistentListOf(
                CategoryProps(
                    id = 1,
                    title = "Fruits",
                ),
                CategoryProps(
                    id = 2,
                    title = "Fish",
                ),
            ),
            selectedOne = null,
        )
    }
}