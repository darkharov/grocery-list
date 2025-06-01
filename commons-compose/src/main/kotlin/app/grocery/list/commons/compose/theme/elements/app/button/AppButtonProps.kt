package app.grocery.list.commons.compose.theme.elements.app.button

import androidx.annotation.DrawableRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.NegativeActionColor
import app.grocery.list.commons.compose.theme.PositiveActionColor

@Stable
sealed class AppButtonProps {

    @get:Stable
    @get:DrawableRes
    abstract val drawableEndId: Int?

    @get:Stable
    abstract val background: Background

    @get:Stable
    abstract val enabled: Boolean

    @Stable
    @Composable
    abstract fun title(): String

    @Immutable
    data class Custom(
        private val text: String,
        override val background: Background = Background.Normal,
        @DrawableRes
        override val drawableEndId: Int? = null,
        override val enabled: Boolean = Defaults.ENABLED,
    ) : AppButtonProps() {

        @Stable
        @Composable
        override fun title(): String =
            text
    }

    @Immutable
    data class Next(
        override val enabled: Boolean = Defaults.ENABLED,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        @Composable
        override fun title() =
            "${stringResource(R.string.next)} >>"
    }

    enum class Background {
        Normal {
            @Composable
            override fun toColor(): Color =
                MaterialTheme.colorScheme.primary
        },
        Positive {
            @Composable
            override fun toColor(): Color =
                PositiveActionColor
        },
        Negative {
            @Composable
            override fun toColor(): Color =
                NegativeActionColor
        },
        ;

        @Stable
        @Composable
        abstract fun toColor(): Color
    }

    private object Defaults {
        const val ENABLED = true
    }
}
