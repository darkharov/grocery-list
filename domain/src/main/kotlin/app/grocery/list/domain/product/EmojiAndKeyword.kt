package app.grocery.list.domain.product

data class EmojiAndKeyword(
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
