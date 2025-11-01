package app.grocery.list.domain.product

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
