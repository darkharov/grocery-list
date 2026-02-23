package app.grocery.list.domain.product.list

data class ProductListsAndSelection(
    val selectedOne: ProductList,
    val productLists: List<ProductList>,
)
