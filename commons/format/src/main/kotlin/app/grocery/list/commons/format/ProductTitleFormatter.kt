package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import app.grocery.list.domain.settings.ProductTitleFormat
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProductTitleFormatter @AssistedInject constructor(
    @Assisted
    private val format: ProductTitleFormat,
    private val errorLogger: ErrorLogger,
) {
    inline fun <R> print(
        products: List<Product>,
        transform: FormattingResult.() -> R,
    ): List<R> =
        products.map { transform(print(it)) }

    fun printToString(products: List<Product>): String =
        print(products) { collectStringTitle() }.joinToString()

    fun print(product: Product): FormattingResult =
        when (format) {
            ProductTitleFormat.WithoutEmoji -> {
                withoutEmoji(product)
            }
            ProductTitleFormat.EmojiAndFullText -> {
                emojiAndFullText(product)
            }
            ProductTitleFormat.EmojiAndAdditionalDetail -> {
                emojiAndAdditionalDetail(product)
            }
        }

    private fun withoutEmoji(product: Product) =
        FormattingResult(
            productId = product.id,
            emoji = null,
            title = product.title,
            additionalDetails = null,
        )

    private fun emojiAndFullText(product: Product) =
        FormattingResult(
            productId = product.id,
            emoji = product.emojiSearchResult?.emoji,
            title = product.title,
            additionalDetails = null,
        )

    private fun emojiAndAdditionalDetail(product: Product): FormattingResult {
        val emojiSearchResult = product.emojiSearchResult
        val title = product.title
        return if (emojiSearchResult == null) {
            withoutEmoji(product)
        } else {
            val emoji = emojiSearchResult.emoji
            val keyword = emojiSearchResult.keyword

            val index = title.indexOf(keyword, ignoreCase = true)
            if (index == -1) {
                errorLogger.log("Unable to extract details from a product ($product)")
                emojiAndFullText(product)
            } else {
                FormattingResult(
                    productId = product.id,
                    emoji = emoji,
                    title = title,
                    additionalDetails = FormattingResult.AdditionalDetails(
                        startIndex = index,
                        length = keyword.length,
                    ),
                )
            }
        }
    }

    data class FormattingResult(
        val productId: Int,
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

    interface ErrorLogger {
        fun log(message: String)
    }

    @AssistedFactory
    internal fun interface Factory {
        fun create(format: ProductTitleFormat): ProductTitleFormatter
    }
}
