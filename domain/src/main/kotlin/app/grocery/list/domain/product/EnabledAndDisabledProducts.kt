package app.grocery.list.domain.product

data class EnabledAndDisabledProducts(
    val all: List<Product>,
    val enabled: List<Product>,
    val disabled: List<Product>,
) {
    val mixed =
        enabled.isNotEmpty() &&
        disabled.isNotEmpty()
}
