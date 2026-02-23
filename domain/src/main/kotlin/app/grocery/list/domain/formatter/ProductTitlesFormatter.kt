package app.grocery.list.domain.formatter

class ProductTitlesFormatter(
    private val separator: Separator,
    private val formatter: ProductTitleFormatter,
) {
    fun print(products: List<ProductTitleFormatter.Params>): String =
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
