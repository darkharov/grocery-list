package app.grocery.list.commons.compose.elements.toolbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.R

@Immutable
sealed interface AppToolbarIconProps {

    @get:DrawableRes
    val iconId: Int

    @get:StringRes
    val descriptionId: Int

    @Immutable
    sealed class Leading : AppToolbarIconProps

    @Immutable
    sealed class Trailing : AppToolbarIconProps

    @Immutable
    data object Up : Leading() {
        override val iconId = R.drawable.ic_back
        override val descriptionId: Int = R.drawable.ic_back
    }

    @Immutable
    data object AllLists : Leading() {
        override val iconId = R.drawable.ic_cards_stack
        override val descriptionId: Int = R.string.all_lists
    }

    @Immutable
    data object Settings : Trailing() {
        override val iconId = R.drawable.ic_settings
        override val descriptionId: Int = R.string.settings
    }
}
