package app.grocery.list.domain.format

enum class ProductListSeparator(
    val value: String,
) {
    Notifications(
        value = ", "
    ),
    Dialog(
        value = "\n",
    ),
}
