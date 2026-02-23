package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun AppDemoColors(
    props: AppColorSchemeProps,
    modifier: Modifier = Modifier,
) {
    val offset = 4.dp
    val cornerRadius = 3.dp
    Spacer(
        modifier = modifier
            .size(18.dp)
            .drawBehind {
                drawRoundRect(
                    color = props.demoColor1,
                    size = size.copy(
                        width = size.width - offset.toPx(),
                        height = size.height - offset.toPx(),
                    ),
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
                drawRoundRect(
                    color = props.demoColor2,
                    topLeft = Offset(offset.toPx(), offset.toPx()),
                    size = size.copy(
                        width = size.width - offset.toPx(),
                        height = size.height - offset.toPx(),
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
        AppDemoColors(AppColorSchemeProps.Yellow)
    }
}
