package app.grocery.list.settings.list.format

import app.grocery.list.domain.settings.ProductTitleFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductItemFormatMapper @Inject constructor() {

    fun toPresentation(entry: ProductTitleFormat) =
        when (entry) {
            ProductTitleFormat.WithoutEmoji -> {
                ListFormatSettingsProps.ProductItemFormat.WithoutEmoji
            }
            ProductTitleFormat.EmojiAndFullText -> {
                ListFormatSettingsProps.ProductItemFormat.EmojiAndFullText
            }
            ProductTitleFormat.EmojiAndAdditionalDetail -> {
                ListFormatSettingsProps.ProductItemFormat.EmojiAndAdditionalDetail
            }
        }

    fun toDomain(entry: ListFormatSettingsProps.ProductItemFormat) =
        when (entry) {
            ListFormatSettingsProps.ProductItemFormat.WithoutEmoji -> {
                ProductTitleFormat.WithoutEmoji
            }
            ListFormatSettingsProps.ProductItemFormat.EmojiAndFullText -> {
                ProductTitleFormat.EmojiAndFullText
            }
            ListFormatSettingsProps.ProductItemFormat.EmojiAndAdditionalDetail -> {
                ProductTitleFormat.EmojiAndAdditionalDetail
            }
        }
}
