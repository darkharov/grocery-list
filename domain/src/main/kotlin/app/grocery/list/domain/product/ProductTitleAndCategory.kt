package app.grocery.list.domain.product

import app.grocery.list.domain.category.Category

data class ProductTitleAndCategory(
    val productTitle: String,
    val category: Category,
)
