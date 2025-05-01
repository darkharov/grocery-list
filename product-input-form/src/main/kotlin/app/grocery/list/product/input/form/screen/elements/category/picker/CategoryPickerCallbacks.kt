package app.grocery.list.product.input.form.screen.elements.category.picker

import androidx.compose.runtime.Stable

@Stable
internal interface CategoryPickerCallbacks {
    fun onCategorySelected(category: CategoryProps)
}

internal object CategoryPickerCallbacksMock : CategoryPickerCallbacks {
    override fun onCategorySelected(category: CategoryProps) {}
}
