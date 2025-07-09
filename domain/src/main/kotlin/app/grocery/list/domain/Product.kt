package app.grocery.list.domain

data class Product(
    val id: Int,
    val title: String,
    val emoji: String?,
    val categoryId: Int,
) {

    data class Category(
        val id: Int,
        val title: String,
    )
}
