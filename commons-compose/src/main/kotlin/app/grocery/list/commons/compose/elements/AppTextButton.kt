package app.grocery.list.commons.compose.elements

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppTextButton(
    text: StringValue,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
    ) {
        Text(
            text = text.value(),
            style = LocalAppTypography.current.button,
        )
    }
}

@Preview
@Composable
private fun AppTextButtonPreview() {
    GroceryListTheme {
        AppTextButton(
            text = StringValue.StringWrapper("Text"),
            onClick = {},
        )
    }
}
