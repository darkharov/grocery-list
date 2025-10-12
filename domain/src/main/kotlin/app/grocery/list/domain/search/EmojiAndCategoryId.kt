package app.grocery.list.domain.search

import app.grocery.list.domain.EmojiSearchResult

data class EmojiAndCategoryId(
    val emoji: EmojiSearchResult?,
    val categoryId: Int,
)
