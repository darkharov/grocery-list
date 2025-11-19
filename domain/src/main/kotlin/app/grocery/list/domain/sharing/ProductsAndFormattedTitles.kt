package app.grocery.list.domain.sharing

import app.grocery.list.domain.product.Product

data class ProductsAndFormattedTitles(
    val products: List<Product>,
    val formattedTitles: String,
)
