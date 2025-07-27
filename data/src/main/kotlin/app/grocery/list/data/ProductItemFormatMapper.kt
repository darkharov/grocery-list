package app.grocery.list.data

import app.grocery.list.domain.settings.ProductItemFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductItemFormatMapper @Inject constructor() {

    fun toInt(format: ProductItemFormat) =
        when (format) {
            ProductItemFormat.WithoutEmoji -> WITHOUT_EMOJI
            ProductItemFormat.EmojiAndFullText -> EMOJI_AND_FULL_TEXT
            ProductItemFormat.EmojiAndAdditionalDetail -> EMOJI_AND_ADDITIONAL_DETAIL
        }

    fun fromInt(id: Int) =
        when (id) {
            WITHOUT_EMOJI -> ProductItemFormat.WithoutEmoji
            EMOJI_AND_FULL_TEXT -> ProductItemFormat.EmojiAndFullText
            EMOJI_AND_ADDITIONAL_DETAIL -> ProductItemFormat.EmojiAndAdditionalDetail
            else -> throw IllegalArgumentException("Unknown id ($id) of product item format.")
        }

    companion object {
        const val WITHOUT_EMOJI = 1
        const val EMOJI_AND_FULL_TEXT = 2
        const val EMOJI_AND_ADDITIONAL_DETAIL = 3
    }
}
