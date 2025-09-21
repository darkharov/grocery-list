package app.grocery.list.domain

data class Product(
    val id: Int,
    val title: String,
    val emojiSearchResult: EmojiSearchResult?,
    val enabled: Boolean,
    val categoryId: Int,
) {

    data class Category(
        val id: Int,
        val title: String,
    )

    data class TitleAndCategory(
        val productTitle: String,
        val category: Category,
    )
}
