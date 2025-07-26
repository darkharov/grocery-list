package app.grocery.list.domain.settings

import app.grocery.list.domain.Product

data class Settings(
    val itemInNotificationMode: ItemInNotificationMode,
) {

    // DO NOT REORDER!
    enum class ItemInNotificationMode {
        WithoutEmoji {
            override fun textForNotification(product: Product): String =
                product.title
        },
        EmojiAndFullText {
            override fun textForNotification(product: Product): String =
                "${product.emojiSearchResult?.emoji.orEmpty()} ${product.title}".trim()
        },
        EmojiAndAdditionalDetail {
            override fun textForNotification(product: Product): String {
                val emojiSearchResult = product.emojiSearchResult
                val title = product.title
                return if (emojiSearchResult == null) {
                    title
                } else {
                    val emoji = emojiSearchResult.emoji
                    val keyword = emojiSearchResult.keyword

                    val index = title.indexOf(keyword, ignoreCase = true)
                    if (index == -1) {
                        title
                    } else {
                        val additionalDetails = title.removeRange(index, index + keyword.length)
                        "$emoji $additionalDetails"
                    }
                }
            }
        },
        ;

        abstract fun textForNotification(product: Product): String

    }
}
