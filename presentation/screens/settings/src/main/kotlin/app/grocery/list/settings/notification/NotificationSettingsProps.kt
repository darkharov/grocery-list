package app.grocery.list.settings.notification

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import app.grocery.list.settings.R

@Immutable
internal class NotificationSettingsProps(
    val itemInNotificationMode: ItemInNotificationMode,
) {
    @Immutable
    enum class ItemInNotificationMode(
        @StringRes
        val titleId: Int,
    ) {
        WithoutEmoji(R.string.without_emoji),
        EmojiAndFullText(R.string.emoji_and_full_text),
        EmojiAndAdditionalDetail(R.string.emoji_and_additional_detail),
    }
}
