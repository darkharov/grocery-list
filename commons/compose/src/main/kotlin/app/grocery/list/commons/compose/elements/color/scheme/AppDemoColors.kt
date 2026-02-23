package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun AppDemoColors(
    size: AppDemoColorsSizeProps,
    colorScheme: AppColorSchemeProps,
    modifier: Modifier = Modifier,
) {
    val offset = size.offset
    val cornerRadius = size.cornerRadius
    Spacer(
        modifier = modifier
            .size(size.size)
            .drawBehind {
                drawRoundRect(
                    color = colorScheme.demoColor1,
                    size = this.size.copy(
                        width = this.size.width - offset.toPx(),
                        height = this.size.height - offset.toPx(),
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
                drawRoundRect(
                    color = colorScheme.demoColor2,
                    topLeft = Offset(offset.toPx(), offset.toPx()),
                    size = this.size.copy(
                        width = this.size.width - offset.toPx(),
                        height = this.size.height - offset.toPx(),
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            },
    )
}

@Preview
@Composable
private fun AppDemoColorsPreview() {
    GroceryListTheme {
        AppDemoColors(
            size = AppDemoColorsSizeProps.Compact,
            colorScheme = AppColorSchemeProps.Yellow,
        )
    }
}
