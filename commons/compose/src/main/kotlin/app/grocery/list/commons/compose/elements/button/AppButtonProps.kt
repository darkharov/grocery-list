package app.grocery.list.commons.compose.elements.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.NegativeActionColor
import app.grocery.list.commons.compose.theme.PositiveActionColor

@Stable
sealed class AppButtonProps {

    @get:DrawableRes
    abstract val drawableEndId: Int?

    @get:Composable
    @get:ReadOnlyComposable
    abstract val title: String

    abstract val background: Background

    protected abstract val state: State

    val enabled: Boolean
        @ReadOnlyComposable
        get() =
            state == State.Enabled

    val progressBar: Boolean
        @ReadOnlyComposable
        get() =
            state == State.DisabledWithProgressBar

    val hasTrailingElement: Boolean
        @ReadOnlyComposable
        get() =
            state == State.DisabledWithProgressBar ||
            drawableEndId != null

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
        val text: String,
        override val background: Background = Background.Normal,
        @DrawableRes
        override val drawableEndId: Int? = null,
        override val state: State = State.Enabled,
    ) : AppButtonProps() {

        override val title: String
            @Composable
            @ReadOnlyComposable
            get() =
                text
    }

    @Immutable
    data class Add(
        override val state: State = State.Enabled,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        override val title: String
            @Composable
            @ReadOnlyComposable
            get() =
                "+ ${stringResource(R.string.add)}"
    }

    @Immutable
    data class Next(
        override val state: State = State.Enabled,
        @StringRes
        private val titleId: Int = R.string.next,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        override val title: String
            @Composable
            @ReadOnlyComposable
            get() =
                "${stringResource(titleId)} >>"
    }

    @Immutable
    data class Done(
        override val state: State = State.Enabled,
    ) : AppButtonProps() {

        override val background = Background.Normal
        override val drawableEndId = null

        override val title: String
            @Composable
            @ReadOnlyComposable
            get() =
                "✓ ${stringResource(R.string.done)}"
    }

    @Immutable
    enum class Background {
        Normal {
            @Composable
            @ReadOnlyComposable
            override fun toColor(): Color =
                MaterialTheme.colorScheme.primary
        },
        Positive {
            @Composable
            @ReadOnlyComposable
            override fun toColor(): Color =
                PositiveActionColor
        },
        Negative {
            @Composable
            @ReadOnlyComposable
            override fun toColor(): Color =
                NegativeActionColor
        },
        ;

        @Composable
        @ReadOnlyComposable
        abstract fun toColor(): Color
    }
}
