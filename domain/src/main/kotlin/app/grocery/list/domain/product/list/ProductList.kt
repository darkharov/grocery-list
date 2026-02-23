package app.grocery.list.domain.product.list

import app.grocery.list.domain.formatter.ProductListStubFormatter
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.EmojiAndKeyword

data class ProductList(
    val id: Id,
    val title: String,
    val colorScheme: ColorScheme,
) {

    sealed interface Id {

        data object Default : Id

        @JvmInline
        value class Custom(val backingId: Int) : Id
    }

    data class CreateParams(
        val title: String,
        val colorScheme: ColorScheme,
    )

    // DO NOT reorder or delete these values
    enum class ColorScheme {
        Yellow,
        Blue,
        Green,
        Magenta,
        ;
    }

    data class Summary(
        val productList: ProductList,
        val size: Int,
        val formattedStub: String,
        val isSelected: Boolean,
    )

    data class RawSummary(
        val productList: ProductList,
        override val totalSize: Int,
        override val items: List<Item>,
    ) : ProductListStubFormatter.ProductListStub {

        data class Item(
            override val title: String,
            override val emojiAndKeyword: EmojiAndKeyword?,
        ) : ProductTitleFormatter.Params
    }
}
