package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppTwoOptionsDialog(
    icon: Painter? = null,
    text: StringValue,
    onDismiss: () -> Unit,
    firstOption: StringValue,
    onFirstOption: () -> Unit,
    secondOption: StringValue,
    onSecondOption: () -> Unit,
    isFirstOptionSensitive: Boolean = false,
) {
    AppTextWithButtonsRowDialog(
        icon = icon,
        text = text,
        onDismiss = onDismiss,
    ) {
        AppTextButton(
            props = AppTextButtonProps.TextOnly(
                text = firstOption,
                color = if (isFirstOptionSensitive) {
                    Color.Red
                } else {
                    Color.Unspecified
                }
            ),
            onClick = onFirstOption,
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        AppTextButton(
            props = AppTextButtonProps.TextOnly(
                text = secondOption,
            ),
            onClick = onSecondOption,
        )
    }
}

@Composable
@Preview
private fun AppTwoOptionsDialogPreview() {
    GroceryListTheme {
        AppTwoOptionsDialog(
            icon = painterResource(R.drawable.ic_android),
            text = StringValue.StringWrapper("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "),
            onDismiss = {},
            firstOption = StringValue.StringWrapper("First Option"),
            onFirstOption = {},
            secondOption = StringValue.StringWrapper("Second Option"),
            onSecondOption = {},
            isFirstOptionSensitive = true,
        )
    }
}
