package app.grocery.list.domain.formatter

class ProductTitlesFormatter(
    private val separator: Separator,
    private val formatter: ProductTitleFormatter,
) {
    fun print(
        products: List<ProductTitleFormatter.Params>,
        ellipsize: Boolean = false,
    ): String =
        buildList {
            addAll(
                products.map { formatter.printToString(it) }
            )
            if (ellipsize) {
                add("…")
            }
        }.joinToString(separator = separator.value)

    fun toStubFormatter() =
        ProductListStubFormatter(this)

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
