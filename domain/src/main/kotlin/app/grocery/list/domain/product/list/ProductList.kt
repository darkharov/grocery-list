package app.grocery.list.domain.product.list

import app.grocery.list.domain.formatter.ProductListStubFormatter
import app.grocery.list.domain.formatter.ProductTitleFormatter
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.theming.ColorScheme
import kotlinx.serialization.Serializable

data class ProductList(
    val id: Id,
    val title: String,
    val colorScheme: ColorScheme,
) {

    sealed interface Id {

        data object Default : Id

        @Serializable
        data class Custom(val backingId: Int) : Id
    }

    data class PutParams(
        val customListId: Id.Custom?,
        val title: String,
        val colorScheme: ColorScheme,
    )

    data class Counters(
        val totalSize: Int,
        val numberOfEnabled: Int,
    )

    data class Summary(
        val listWithCounters: WithCounters,
        val formattedStub: String,
        val isSelected: Boolean,
    ) {
        val counters get() = listWithCounters.counters
        val productList get() = listWithCounters.productList
    }

    data class RawSummary(
        val listWithCounters: WithCounters,
        override val items: List<Item>,
    ) : ProductListStubFormatter.ProductListStub {

        val counters get() = listWithCounters.counters
        val productList get() = listWithCounters.productList

        override val totalSize = counters.totalSize

        data class Item(
            override val title: String,
            override val emojiAndKeyword: EmojiAndKeyword?,
        ) : ProductTitleFormatter.Params
    }

    data class WithCounters(
        val productList: ProductList,
        val counters: Counters,
    )

    data class Neighbours(
        val trailing: WithCounters?,
        val leading: WithCounters?,
    )
}
