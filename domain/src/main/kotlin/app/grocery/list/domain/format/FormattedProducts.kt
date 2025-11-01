package app.grocery.list.domain.format

import app.grocery.list.domain.Product

data class FormattedProducts(
    val formattedString: String,
    val originalProducts: List<Product>,
)
