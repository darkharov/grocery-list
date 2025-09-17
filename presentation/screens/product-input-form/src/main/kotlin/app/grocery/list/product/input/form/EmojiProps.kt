package app.grocery.list.product.input.form

import androidx.compose.runtime.Immutable

@Immutable
data class EmojiProps(
    val value: String,
    val payload: Any? = null
)
