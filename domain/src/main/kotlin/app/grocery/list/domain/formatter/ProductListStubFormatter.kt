package app.grocery.list.domain.formatter

class ProductListStubFormatter(
    private val formatter: ProductTitlesFormatter,
) {
    fun print(stub: ProductListStub): String {
        val items = stub.items
        val totalSize = stub.totalSize
        return formatter.print(
            products = items,
            ellipsize = totalSize > items.size,
        )
    }

    interface ProductListStub {
        val totalSize: Int
        val items: List<ProductTitleFormatter.Params>
    }
}
