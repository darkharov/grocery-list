package app.grocery.list.domain.formatter

import app.grocery.list.domain.product.Product

sealed class ProductTitleFormatter {

    abstract fun print(product: Product): Result

    fun printToString(product: Product): String =
        print(product).collectStringTitle()

    fun withCommas(): ProductTitlesFormatter =
        ProductTitlesFormatter(
            formatter = this,
            separator = ProductTitlesFormatter.Separator.Comma,
        )

    fun withNewLines(): ProductTitlesFormatter =
        ProductTitlesFormatter(
            formatter = this,
            separator = ProductTitlesFormatter.Separator.NewLine,
        )

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
                append(title.trim())
            }

        data class AdditionalDetails(
            val startIndex: Int,
            val length: Int,
        ) {
            val endIndex = startIndex + length
        }
    }
}

data object ProductEmojiAndFullTextFormatter : ProductTitleFormatter() {

    override fun print(product: Product): Result =
        Result(
            emoji = product.emojiSearchResult?.emoji,
            title = product.title,
            additionalDetails = null,
        )
}

data object ProductEmojiAndAdditionalDetailsFormatter : ProductTitleFormatter() {

    override fun print(product: Product): Result {
        val emojiSearchResult = product.emojiSearchResult
        val title = product.title
        return if (emojiSearchResult == null) {
            ProductTitleWithoutEmojiFormatter.print(product)
        } else {
            val emoji = emojiSearchResult.emoji
            val keyword = emojiSearchResult.keyword

            val index = title.indexOf(keyword, ignoreCase = true)
            if (index == -1) {
                ProductEmojiAndFullTextFormatter.print(product)
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
}

data object ProductTitleWithoutEmojiFormatter : ProductTitleFormatter() {

    override fun print(product: Product): Result =
        Result(
            emoji = null,
            title = product.title,
            additionalDetails = null,
        )
}
