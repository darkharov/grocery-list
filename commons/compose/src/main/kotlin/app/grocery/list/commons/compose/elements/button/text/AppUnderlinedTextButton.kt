package app.grocery.list.commons.compose.elements.button.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

@Composable
fun AppUnderlinedTextButton(
    text: StringValue,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.value(),
        style = LocalAppTypography.current.plainText,
        textDecoration = TextDecoration.Underline,
        color = LocalAppColors.current.blackOrWhite,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .padding(
                top = 6.dp,
                bottom = 10.dp,
            ),
    )
}

@Preview
@Composable
private fun AppUnderlinedTextButtonPreview() {
    GroceryListTheme {
        AppUnderlinedTextButton(
            text = StringValue.StringWrapper("Text"),
            onClick = {},
        )
    }
}