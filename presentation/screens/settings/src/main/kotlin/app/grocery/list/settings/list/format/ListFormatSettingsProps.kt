package app.grocery.list.settings.list.format

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import app.grocery.list.settings.R

@Immutable
internal class ListFormatSettingsProps(
    val productTitleFormat: ProductTitleFormat,
    val sampleOfNotificationTitle: String,
) {
    @Immutable
    enum class ProductTitleFormat(
        @StringRes
        val titleId: Int,
    ) {
        WithoutEmoji(R.string.without_emoji),
        EmojiAndFullText(R.string.emoji_and_full_text),
        EmojiAndAdditionalDetail(R.string.emoji_and_additional_details_only),
    }
}
