package app.grocery.list.commons.compose.elements.button.text

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.blackOrWhite
import app.grocery.list.commons.compose.values.StringValue

@Immutable
sealed class AppTextButtonProps {

    internal abstract val text: StringValue

    @get:Composable
    internal abstract val textColor: Color

    @get:DrawableRes
    internal abstract val leadingIconId: Int?

    @get:DrawableRes
    internal abstract val trailingIconId: Int?

    internal abstract val hasDivider: Boolean

    internal abstract val enabled: Boolean

    internal open val padding = Defaults.Padding

    @Immutable
    class TextOnly(
        override val text: StringValue,
        override val enabled: Boolean = true,
        override val padding: PaddingValues = Defaults.Padding,
        val color: Color = Color.Unspecified,
    ) : AppTextButtonProps() {

        override val leadingIconId = null
        override val trailingIconId = null
        override val hasDivider = false

        override val textColor: Color
            @Composable
            @ReadOnlyComposable
            get() =
                when {
                    color != Color.Unspecified -> color
                    else -> MaterialTheme.colorScheme.secondary
                }
    }

    @Immutable
    data class SettingsCategory(
        override val text: StringValue,
        @DrawableRes
        override val leadingIconId: Int,
    ) : AppTextButtonProps() {

        override val trailingIconId = R.drawable.ic_forward
        override val hasDivider = true
        override val enabled = true

        override val textColor: Color
            @Composable
            get() =
                MaterialTheme.colorScheme.blackOrWhite
                    .copy(alpha = 0.88f)
    }

    object Defaults {
        val Padding = PaddingValues(
            vertical = 12.dp,
            horizontal = 16.dp,
        )
    }
}


internal class AppTextButtonMocks : PreviewParameterProvider<AppTextButtonProps> {
    override val values = sequenceOf(
        AppTextButtonProps.TextOnly(
            text = StringValue.StringWrapper("Text"),
        ),
        AppTextButtonProps.SettingsCategory(
            text = StringValue.StringWrapper("Text"),
            leadingIconId = R.drawable.ic_android,
        )
    )
}