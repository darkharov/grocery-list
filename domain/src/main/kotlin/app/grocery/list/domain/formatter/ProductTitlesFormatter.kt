package app.grocery.list.domain.formatter

import app.grocery.list.domain.product.Product

class ProductTitlesFormatter(
    private val separator: Separator,
    private val formatter: ProductTitleFormatter,
) {
    fun print(products: List<Product>): String =
        products.joinToString(
            separator = separator.value,
            transform = formatter::printToString,
        )

    enum class Separator(
        val value: String,
    ) {
        Comma(
            value = ", "
        ),
        NewLine(
            value = "\n",
        ),
    }
}
