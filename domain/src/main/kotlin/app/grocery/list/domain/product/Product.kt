package app.grocery.list.domain.product

data class Product(
    val id: Int,
    val title: String,
    val emojiSearchResult: EmojiSearchResult?,
    val enabled: Boolean,
    val categoryId: Int,
) {
    val disabled = !(enabled)
}
