package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.Product

data class ParsedProducts(
    val formattedString: String,
    val originalProducts: List<Product>,
)
