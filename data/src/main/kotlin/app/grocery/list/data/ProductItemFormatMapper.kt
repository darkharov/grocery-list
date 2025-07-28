package app.grocery.list.data

import app.grocery.list.domain.settings.ProductTitleFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductItemFormatMapper @Inject constructor() {

    fun toInt(format: ProductTitleFormat) =
        when (format) {
            ProductTitleFormat.WithoutEmoji -> WITHOUT_EMOJI
            ProductTitleFormat.EmojiAndFullText -> EMOJI_AND_FULL_TEXT
            ProductTitleFormat.EmojiAndAdditionalDetail -> EMOJI_AND_ADDITIONAL_DETAIL
        }

    fun fromInt(id: Int) =
        when (id) {
            WITHOUT_EMOJI -> ProductTitleFormat.WithoutEmoji
            EMOJI_AND_FULL_TEXT -> ProductTitleFormat.EmojiAndFullText
            EMOJI_AND_ADDITIONAL_DETAIL -> ProductTitleFormat.EmojiAndAdditionalDetail
            else -> throw IllegalArgumentException("Unknown id ($id) of product item format.")
        }

    companion object {
        const val WITHOUT_EMOJI = 1
        const val EMOJI_AND_FULL_TEXT = 2
        const val EMOJI_AND_ADDITIONAL_DETAIL = 3
    }
}
