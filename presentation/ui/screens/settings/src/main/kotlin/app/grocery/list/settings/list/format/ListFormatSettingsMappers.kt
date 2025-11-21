package app.grocery.list.settings.list.format

import app.grocery.list.domain.settings.ProductTitleFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductTitleFormatMapper @Inject constructor() {

    fun toPresentation(entry: ProductTitleFormat) =
        when (entry) {
            ProductTitleFormat.WithoutEmoji -> {
                ListFormatSettingsProps.ProductTitleFormat.WithoutEmoji
            }
            ProductTitleFormat.EmojiAndFullText -> {
                ListFormatSettingsProps.ProductTitleFormat.EmojiAndFullText
            }
            ProductTitleFormat.EmojiAndAdditionalDetails -> {
                ListFormatSettingsProps.ProductTitleFormat.EmojiAndAdditionalDetail
            }
        }

    fun toDomain(entry: ListFormatSettingsProps.ProductTitleFormat) =
        when (entry) {
            ListFormatSettingsProps.ProductTitleFormat.WithoutEmoji -> {
                ProductTitleFormat.WithoutEmoji
            }
            ListFormatSettingsProps.ProductTitleFormat.EmojiAndFullText -> {
                ProductTitleFormat.EmojiAndFullText
            }
            ListFormatSettingsProps.ProductTitleFormat.EmojiAndAdditionalDetail -> {
                ProductTitleFormat.EmojiAndAdditionalDetails
            }
        }
}
