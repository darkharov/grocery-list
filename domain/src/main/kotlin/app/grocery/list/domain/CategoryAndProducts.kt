package app.grocery.list.domain

data class CategoryAndProducts(
    val category: Product.Category,
    val products: List<Product>,
)
