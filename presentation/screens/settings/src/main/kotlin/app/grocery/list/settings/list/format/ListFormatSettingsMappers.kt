package app.grocery.list.settings.list.format

import app.grocery.list.domain.settings.ProductItemFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProductItemFormatMapper @Inject constructor() {

    fun toPresentation(entry: ProductItemFormat) =
        when (entry) {
            ProductItemFormat.WithoutEmoji -> {
                ListFormatSettingsProps.ProductItemFormat.WithoutEmoji
            }
            ProductItemFormat.EmojiAndFullText -> {
                ListFormatSettingsProps.ProductItemFormat.EmojiAndFullText
            }
            ProductItemFormat.EmojiAndAdditionalDetail -> {
                ListFormatSettingsProps.ProductItemFormat.EmojiAndAdditionalDetail
            }
        }

    fun toDomain(entry: ListFormatSettingsProps.ProductItemFormat) =
        when (entry) {
            ListFormatSettingsProps.ProductItemFormat.WithoutEmoji -> {
                ProductItemFormat.WithoutEmoji
            }
            ListFormatSettingsProps.ProductItemFormat.EmojiAndFullText -> {
                ProductItemFormat.EmojiAndFullText
            }
            ListFormatSettingsProps.ProductItemFormat.EmojiAndAdditionalDetail -> {
                ProductItemFormat.EmojiAndAdditionalDetail
            }
        }
}
