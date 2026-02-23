package app.grocery.list.domain.formatter

import app.grocery.list.domain.product.EmojiAndKeyword

sealed class ProductTitleFormatter {

    abstract fun print(params: Params): Result

    fun printToString(params: Params): String =
        print(params).collectStringTitle()

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

    interface Params {
        val title: String
        val emojiAndKeyword: EmojiAndKeyword?
    }

    data class Result(
        val emoji: String?,
        val title: String,
        val additionalDetails: AdditionalDetails?,
    ) {
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

    override fun print(params: Params): Result =
        Result(
            emoji = params.emojiAndKeyword?.emoji,
            title = params.title,
            additionalDetails = null,
        )
}

data object ProductEmojiAndAdditionalDetailsFormatter : ProductTitleFormatter() {

    override fun print(params: Params): Result {
        val emojiAndKeyword = params.emojiAndKeyword
        val title = params.title
        return if (emojiAndKeyword == null) {
            ProductTitleWithoutEmojiFormatter.print(params)
        } else {
            val emoji = emojiAndKeyword.emoji
            val keyword = emojiAndKeyword.keyword

            val index = title.indexOf(keyword, ignoreCase = true)
            if (index == -1) {
                ProductEmojiAndFullTextFormatter.print(params)
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

    override fun print(params: Params): Result =
        Result(
            emoji = null,
            title = params.title,
            additionalDetails = null,
        )
}
