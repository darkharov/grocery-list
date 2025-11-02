package app.grocery.list.domain.product

data class CategoryProducts(
    val categoryId: Int,
    val products: List<Product>,
) {
    val hasEnabledProducts get() = products.any { it.enabled }
    val hasDisabledProducts get() = products.any { it.disabled }
}
