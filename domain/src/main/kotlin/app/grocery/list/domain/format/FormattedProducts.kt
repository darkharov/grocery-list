package app.grocery.list.domain.format

import app.grocery.list.domain.Product

data class FormattedProducts(
    val items: List<Product>,
    val formattedString: String,
)
