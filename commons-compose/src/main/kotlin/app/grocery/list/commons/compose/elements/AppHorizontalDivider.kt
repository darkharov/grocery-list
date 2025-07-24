package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.AppHorizontalDividerMode.Shadow.Downward
import app.grocery.list.commons.compose.elements.AppHorizontalDividerMode.Shadow.Upward
import app.grocery.list.commons.compose.theme.GroceryListTheme

private val colors = listOf(
    Color.Transparent,
    Color.Black.copy(alpha = 0.05f),
)

@Immutable
sealed class AppHorizontalDividerMode {

    @Immutable
    data class DividerOnly(
        val useScreenOffset: Boolean = false,
    ) : AppHorizontalDividerMode()

    @Immutable
    sealed class Shadow(
        colors: List<Color>,
        val height: Dp,
    ) : AppHorizontalDividerMode() {
        internal val brush = Brush.verticalGradient(colors)

        data object Upward : Shadow(
            colors = colors,
            height = 12.dp,
        )

        data object Downward : Shadow(
            colors = colors.reversed(),
            height = 8.dp
        )
    }
}

@Composable
fun AppHorizontalDivider(
    modifier: Modifier = Modifier,
    mode: AppHorizontalDividerMode = AppHorizontalDividerMode.DividerOnly(),
) {
    when (mode) {
        is AppHorizontalDividerMode.DividerOnly -> {
            NativeDivider(
                modifier = modifier
                    .then(
                        if (mode.useScreenOffset) {
                            Modifier.padding(
                                horizontal = dimensionResource(R.dimen.margin_16_32_64),
                            )
                        } else {
                            Modifier
                        },
                    ),
            )
        }
        is AppHorizontalDividerMode.Shadow -> {
            if (isSystemInDarkTheme()) {
                when (mode) {
                    is Upward -> {
                        NativeDivider(
                            modifier = modifier,
                        )
                    }
                    is Downward -> {
                        // imho it is not pretty, so nothing to show
                    }
                }
            } else {
                Spacer(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(mode.height)
                        .drawWithCache {
                            onDrawBehind {
                                drawLine(
                                    brush = mode.brush,
                                    start = Offset(0f, size.height / 2f),
                                    end = Offset(size.width, size.height / 2f),
                                    strokeWidth = size.height,
                                )
                            }
                        },
                )
            }
        }
    }
}

@Composable
fun AppHorizontalDividerWithOffset() {
    AppHorizontalDivider(
        mode = AppHorizontalDividerMode.DividerOnly(
            useScreenOffset = true,
        ),
    )
}

@Composable
private fun NativeDivider(modifier: Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = Color.Gray.copy(alpha = 0.2f),
    )
}

@Composable
@PreviewLightDark
private fun HorizontalShadowPreview() {
    GroceryListTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(vertical = 16.dp),
        ) {
            listOf(
                Upward,
                Downward,
                AppHorizontalDividerMode.DividerOnly(),
            ).forEach { mode ->
                AppHorizontalDivider(
                    mode = mode,
                    modifier = Modifier,
                )
            }
        }
    }
}
