package app.grocery.list.settings.notification

import app.grocery.list.domain.settings.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ItemInNotificationModeMapper @Inject constructor() {

    fun toPresentation(entry: Settings.ItemInNotificationMode) =
        when (entry) {
            Settings.ItemInNotificationMode.WithoutEmoji -> {
                NotificationSettingsProps.ItemInNotificationMode.WithoutEmoji
            }
            Settings.ItemInNotificationMode.EmojiAndFullText -> {
                NotificationSettingsProps.ItemInNotificationMode.EmojiAndFullText
            }
            Settings.ItemInNotificationMode.EmojiAndAdditionalDetail -> {
                NotificationSettingsProps.ItemInNotificationMode.EmojiAndAdditionalDetail
            }
        }

    fun toDomain(entry: NotificationSettingsProps.ItemInNotificationMode) =
        when (entry) {
            NotificationSettingsProps.ItemInNotificationMode.WithoutEmoji -> {
                Settings.ItemInNotificationMode.WithoutEmoji
            }
            NotificationSettingsProps.ItemInNotificationMode.EmojiAndFullText -> {
                Settings.ItemInNotificationMode.EmojiAndFullText
            }
            NotificationSettingsProps.ItemInNotificationMode.EmojiAndAdditionalDetail -> {
                Settings.ItemInNotificationMode.EmojiAndAdditionalDetail
            }
        }
}
