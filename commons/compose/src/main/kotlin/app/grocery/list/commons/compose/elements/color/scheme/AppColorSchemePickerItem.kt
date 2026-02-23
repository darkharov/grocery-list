package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme

private val SelectionIndicator = Color.Gray.copy(alpha = 0.1f)

@Composable
fun AppColorSchemePickerItem(
    value: AppColorSchemeProps,
    selected: Boolean,
    onSelect: (value: AppColorSchemeProps) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerRadius = 4.dp
    val color = animateColorAsState(
        targetValue = if (selected) {
            SelectionIndicator
        } else {
            Color.Transparent
        },
    )
    Box(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = color.value,
                    cornerRadius = CornerRadius(cornerRadius.toPx()),
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onSelect(value) },
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        AppDemoColors(
            props = value,
        )
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun AppColorSchemePickerItemPreview() {
    GroceryListTheme {
        var selected by remember { mutableStateOf(false) }
        AppColorSchemePickerItem(
            value = AppColorSchemeProps.Yellow,
            selected = selected,
            onSelect = {
                selected = !selected
            },
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
