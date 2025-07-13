package app.grocery.list.commons.compose.elements.app.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    protected abstract val state: State

    @Stable
    @Composable
    abstract fun title(): String

    val enabled @Stable get() = state == State.Enabled
    val progressBar @Stable get() = state == State.DisabledWithProgressBar
    val hasTrailingElement @Stable get() = state == State.DisabledWithProgressBar || drawableEndId != null

    @Immutable
    enum class State {
        Enabled,
        Disabled,
        DisabledWithProgressBar,
        ;

        companion object {

            @Stable
            fun enabled(enabled: Boolean) =
                if (enabled) {
                    Enabled
                } else {
                    Disabled
                }

            @Stable
            fun progress(progress: Boolean) =
                if (progress) {
                    DisabledWithProgressBar
                } else {
                    Enabled
                }
        }
    }

    @Immutable
    data class Custom(
        private val text: String,
        override val background: Background = Background.Normal,
        @DrawableRes
        override val drawableEndId: Int? = null,
        override val state: State = State.Enabled,
    ) : AppButtonProps() {

        @Stable
        @Composable
        override fun title(): String =
            text
    }

    @Immutable
    data class Next(
        override val state: State = State.Enabled,
        @StringRes
        private val titleId: Int = R.string.next,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        @Composable
        override fun title() =
            "${stringResource(titleId)} >>"
    }

    @Immutable
    data class Done(
        override val state: State = State.Enabled,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        @Composable
        override fun title() =
            "✓ ${stringResource(R.string.done)}"
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
}
