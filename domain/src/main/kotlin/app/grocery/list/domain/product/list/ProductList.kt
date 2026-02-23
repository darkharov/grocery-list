package app.grocery.list.domain.product.list

import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.EmojiAndKeyword

data class ProductList(
    val id: Id,
    val title: String,
    val colorScheme: ColorScheme,
) {
    sealed class Id {
        data object Default : Id()
        data class Custom(val id: Int) : Id()
    }

    // DO NOT reorder or delete these values
    enum class ColorScheme {
        Yellow,
        Blue,
        Green,
        Magenta,
    }

    data class RawSummary(
        val productList: ProductList,
        val totalSize: Int,
        val sampleItems: List<SampleItem>,
    ) {
        data class SampleItem(
            override val title: String,
            override val emojiAndKeyword: EmojiAndKeyword?,
        ) : ProductTitleFormatter.Params
    }

    data class FormattedSummary(
        val productList: ProductList,
        val totalSize: Int,
        val stub: String,
    )

    data class CreateParams(
        val title: String,
        val colorScheme: ColorScheme,
    )
}
