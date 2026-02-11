package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.text.AppTextButton2
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppTwoOptionsDialog(
    icon: Painter? = null,
    text: StringValue,
    onDismiss: () -> Unit,
    firstOption: StringValue,
    isFirstOptionSensitive: Boolean = false,
    onFirstOption: () -> Unit,
    secondOption: StringValue = StringValue.ResId(android.R.string.cancel),
    onSecondOption: () -> Unit,
) {
    AppTextWithButtonsRowDialog(
        icon = icon,
        text = text,
        onDismiss = onDismiss,
    ) {
        AppTextButton2(
            text = firstOption,
            dangerous = isFirstOptionSensitive,
            onClick = onFirstOption,
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
        )
        AppTextButton2(
            text = secondOption,
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
