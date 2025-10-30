package app.grocery.list.domain

data class CategoryAndProducts(
    val category: Product.Category,
    val products: List<Product>,
) {
    val hasEnabledProducts get() = products.any { it.enabled }
    val hasDisabledProducts get() = products.any { it.disabled }
}
