package app.grocery.list.domain.format

import app.grocery.list.domain.product.Product

// Do not reorder. The 'ordinal' field is used as a key for read/write operations
enum class ProductTitleFormatter {
    WithoutEmoji {
        override fun print(product: Product): Result =
            Result(
                emoji = null,
                title = product.title,
                additionalDetails = null,
            )
    },
    EmojiAndFullText {
        override fun print(product: Product): Result =
            Result(
                emoji = product.emojiSearchResult?.emoji,
                title = product.title,
                additionalDetails = null,
            )
    },
    EmojiAndAdditionalDetail {
        override fun print(product: Product): Result {
            val emojiSearchResult = product.emojiSearchResult
            val title = product.title
            return if (emojiSearchResult == null) {
                WithoutEmoji.print(product)
            } else {
                val emoji = emojiSearchResult.emoji
                val keyword = emojiSearchResult.keyword

                val index = title.indexOf(keyword, ignoreCase = true)
                if (index == -1) {
                    EmojiAndFullText.print(product)
                } else {
                    Result(
                        emoji = emoji,
                        title = title,
                        additionalDetails = Result.AdditionalDetails(
                            startIndex = index,
                            length = keyword.length,
                        ),
                    )
                }
            }
        }
    },
    ;

    abstract fun print(product: Product): Result

    fun print(
        products: List<Product>,
        separator: ProductListSeparator,
    ): String =
        products.joinToString(separator = separator.value) { product ->
            print(product).collectStringTitle()
        }

    data class Result(
        val emoji: String?,
        val title: String,
        val additionalDetails: AdditionalDetails?,
    ) {
        @Deprecated("It is probably a mistake", ReplaceWith("this.collectStringTitle()"))
        override fun toString() =
            super.toString()

        fun collectStringTitle(): String =
            buildString {
                if (!(emoji.isNullOrBlank())) {
                    append(emoji)
                    append(' ')
                }
                val additionalDetailsLocation = additionalDetails
                val title = if (additionalDetailsLocation != null) {
                    title.removeRange(
                        startIndex = additionalDetailsLocation.startIndex,
                        endIndex = additionalDetailsLocation.endIndex,
                    )
                } else {
                    title
                }
                append(title)
            }

        data class AdditionalDetails(
            val startIndex: Int,
            val length: Int,
        ) {
            val endIndex = startIndex + length
        }
    }
}
