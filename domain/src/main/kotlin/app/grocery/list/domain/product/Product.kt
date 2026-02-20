package app.grocery.list.domain.product

data class Product(
    val id: Int,
    val title: String,
    val emojiAndKeyword: EmojiAndKeyword?,
    val enabled: Boolean,
    val categoryId: Int,
    val customListId: Int?,
) {
    val disabled = !(enabled)
}
