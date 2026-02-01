package app.grocery.list.product.input.form.elements.category.picker

import androidx.compose.runtime.Stable

@Stable
internal interface CategoryPickerCallbacks {
    fun onCategoryPickerExpandChange(expanded: Boolean)
    fun onCategorySelected(categoryId: Int)
}

internal object CategoryPickerCallbacksMock : CategoryPickerCallbacks {
    override fun onCategoryPickerExpandChange(expanded: Boolean) {}
    override fun onCategorySelected(categoryId: Int) {}
}
