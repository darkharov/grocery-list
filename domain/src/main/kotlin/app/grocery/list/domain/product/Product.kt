package app.grocery.list.domain.product

import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.list.ProductList

data class Product(
    val id: Int,
    override val title: String,
    override val emojiAndKeyword: EmojiAndKeyword?,
    val enabled: Boolean,
    val categoryId: Int,
    val productListId: ProductList.Id,
) : ProductTitleFormatter.Params {

    val disabled = !(enabled)

    data class Criteria(
        val enabledOnly: Boolean,
        val productListId: ProductList.Id,
    )
}
