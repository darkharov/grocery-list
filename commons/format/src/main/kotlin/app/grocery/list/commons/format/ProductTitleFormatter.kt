package app.grocery.list.commons.format

import app.grocery.list.domain.Product
import app.grocery.list.domain.settings.ProductTitleFormat
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ProductTitleFormatter @AssistedInject constructor(
    @Assisted
    private val format: ProductTitleFormat,
    //private val errorLogger: ErrorLogger,
) {
    fun print(product: Product): FormattingResult =
        when (format) {
            ProductTitleFormat.WithoutEmoji -> {
                withoutEmoji(product)
            }
            ProductTitleFormat.EmojiAndFullText -> {
                emojiAndFullText(product)
            }
            ProductTitleFormat.EmojiAndAdditionalDetail -> {
                val emojiSearchResult = product.emojiSearchResult
                val title = product.title
                if (emojiSearchResult == null) {
                    withoutEmoji(product)
                } else {
                    val emoji = emojiSearchResult.emoji
                    val keyword = emojiSearchResult.keyword

                    val index = title.indexOf(keyword, ignoreCase = true)
                    if (index == -1) {
                        //errorLogger.log("Unable to extract details from a product ($product)")
                        emojiAndFullText(product)
                    } else {
                        FormattingResult(
                            emoji = emoji,
                            title = title,
                            additionalDetailsLocation = FormattingResult.AdditionalDetailsLocation(
                                startIndex = index,
                                length = keyword.length,
                            ),
                        )
                    }
                }
            }
        }

    private fun emojiAndFullText(product: Product) =
        FormattingResult(
            emoji = product.emojiSearchResult?.emoji,
            title = product.title,
            additionalDetailsLocation = null,
        )

    private fun withoutEmoji(product: Product) = FormattingResult(
        emoji = null,
        title = product.title,
        additionalDetailsLocation = null,
    )

    data class FormattingResult(
        val emoji: String?,
        val title: String,
        val additionalDetailsLocation: AdditionalDetailsLocation?,
    ) {
        data class AdditionalDetailsLocation(
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
    fun interface Factory {
        fun create(format: ProductTitleFormat): ProductTitleFormatter
    }
}
