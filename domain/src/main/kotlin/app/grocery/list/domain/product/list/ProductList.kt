package app.grocery.list.domain.product.list

sealed class ProductList {

    data object Default : ProductList()

    data class Custom(
        val id: Int,
        val title: String,
        val colorScheme: ColorScheme,
    ) : ProductList() {

        // Do not reorder or delete these values
        enum class ColorScheme {
            Orange,
            Blue,
            Green,
            Magenta,
        }

        data class CreateParams(
            val title: String,
            val colorScheme: ColorScheme,
        )

        data class Summary(
            val title: String,
            val colorScheme: ColorScheme,
            val itemCount: Int,
            val stub: String,
        )
    }
}
