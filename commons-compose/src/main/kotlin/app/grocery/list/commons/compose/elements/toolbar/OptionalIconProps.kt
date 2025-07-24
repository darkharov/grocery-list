package app.grocery.list.commons.compose.elements.toolbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
internal data class OptionalIconProps(
    val type: Type,
    val content: Content?,
) {
    data class Content(

        @get:DrawableRes
        val iconId: Int,

        @get:StringRes
        val descriptionId: Int?,
    )

    @Immutable
    enum class Type {
        Leading,
        Trailing,
    }
}
