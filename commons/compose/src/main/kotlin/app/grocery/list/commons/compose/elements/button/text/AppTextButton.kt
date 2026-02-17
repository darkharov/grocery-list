package app.grocery.list.commons.compose.elements.button.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

@Composable
fun AppTextButton(
    text: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    padding: PaddingValues = PaddingValues(
        vertical = 12.dp,
        horizontal = 16.dp,
    ),
    dangerous: Boolean = false,
) {
    Text(
        text = text.value(),
        color = if (dangerous) {
            Color.Red
        } else {
            MaterialTheme.colorScheme.secondary
        },
        textAlign = TextAlign.Center,
        style = LocalAppTypography.current.textButton,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (enabled) {
                    Modifier.clickable {
                        onClick()
                    }
                } else {
                    Modifier
                        .alpha(0.33f)
                }
            )
            .padding(padding),
    )
}

@PreviewLightDark
@Composable
private fun AppTextButtonPreview() {
    GroceryListTheme {
        AppTextButton(
            text = StringValue.StringWrapper("Text"),
            dangerous = false,
            enabled = true,
            onClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun AppTextButtonDangerousPreview() {
    GroceryListTheme {
        AppTextButton(
            text = StringValue.StringWrapper("Text"),
            dangerous = true,
            enabled = true,
            onClick = {},
        )
    }
}
