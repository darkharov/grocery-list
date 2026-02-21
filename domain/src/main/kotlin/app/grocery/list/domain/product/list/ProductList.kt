package app.grocery.list.domain.product.list

sealed class ProductList {

    data object Default : ProductList()

    data class Custom(
        val id: Int,
        val title: String,
    ) : ProductList() {

        data class CreateParams(
            val title: String,
        )
    }

    data class Summary(
        val itemCount: Int,
        val stub: String,
    )
}
