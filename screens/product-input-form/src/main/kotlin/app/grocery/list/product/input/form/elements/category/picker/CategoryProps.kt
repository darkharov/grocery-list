package app.grocery.list.product.input.form.elements.category.picker

import androidx.compose.runtime.Immutable

@Immutable
internal data class CategoryProps(
    val id: Int,
    val title: String,
    val payload: Any? = null,
)
