package app.grocery.list.settings.list.format

import app.grocery.list.domain.format.ProductTitleFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductTitleFormatMapper @Inject constructor() {

    fun toPresentation(entry: ProductTitleFormatter) =
        when (entry) {
            ProductTitleFormatter.WithoutEmoji -> {
                ListFormatSettingsProps.ProductTitleFormat.WithoutEmoji
            }
            ProductTitleFormatter.EmojiAndFullText -> {
                ListFormatSettingsProps.ProductTitleFormat.EmojiAndFullText
            }
            ProductTitleFormatter.EmojiAndAdditionalDetail -> {
                ListFormatSettingsProps.ProductTitleFormat.EmojiAndAdditionalDetail
            }
        }

    fun toDomain(entry: ListFormatSettingsProps.ProductTitleFormat) =
        when (entry) {
            ListFormatSettingsProps.ProductTitleFormat.WithoutEmoji -> {
                ProductTitleFormatter.WithoutEmoji
            }
            ListFormatSettingsProps.ProductTitleFormat.EmojiAndFullText -> {
                ProductTitleFormatter.EmojiAndFullText
            }
            ListFormatSettingsProps.ProductTitleFormat.EmojiAndAdditionalDetail -> {
                ProductTitleFormatter.EmojiAndAdditionalDetail
            }
        }
}
