package app.grocery.list.domain.product

import app.grocery.list.domain.formatter.ProductTitleFormatter

data class Product(
    val id: Int,
    override val title: String,
    override val emojiAndKeyword: EmojiAndKeyword?,
    val enabled: Boolean,
    val categoryId: Int,
    val customListId: Int?,
) : ProductTitleFormatter.Params {
    val disabled = !(enabled)
}
