package app.grocery.list.commons.compose.elements.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.NegativeActionColor
import app.grocery.list.commons.compose.theme.PositiveActionColor

@Immutable
enum class AppButtonBackgroundProps {
    Normal {
        @Composable
        @ReadOnlyComposable
        override fun toColor(): Color =
            LocalAppColors.current.brand_60_50
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

internal class AppButtonBackgroundMocks : PreviewParameterProvider<AppButtonBackgroundProps> {
    override val values = AppButtonBackgroundProps.entries.asSequence()
}
