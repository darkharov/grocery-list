package app.grocery.list.domain

data class EmojiSearchResult(
    val emoji: String,
    val keyword: String,
) {
    init {
        assert(
            emoji.isNotBlank() &&
            keyword.isNotBlank()
        )
    }
}
