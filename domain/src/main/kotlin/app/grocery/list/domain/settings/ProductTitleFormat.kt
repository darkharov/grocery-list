package app.grocery.list.domain.settings

import app.grocery.list.domain.internal.ONLY_FOR_MIGRATION

@Deprecated(ONLY_FOR_MIGRATION)
enum class ProductTitleFormat {
    WithoutEmoji,
    EmojiAndFullText,
    EmojiAndAdditionalDetail,
    Null,
}
