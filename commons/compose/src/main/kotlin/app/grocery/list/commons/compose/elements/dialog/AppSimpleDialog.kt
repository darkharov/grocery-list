package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppSimpleDialog(
    icon: Painter,
    text: StringValue,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: StringValue = StringValue.ResId(android.R.string.ok),
    onCancel: (() -> Unit)? = null,
) {
    AppTextWithButtonsRowDialog(
        icon = icon,
        text = text,
        onDismiss = onDismiss,
        buttons = {
            if (onCancel != null) {
                AppTextButton(
                    text = StringValue.ResId(android.R.string.cancel),
                    onClick = onCancel,
                )
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                )
            }
            AppTextButton(
                text = confirmButtonText,
                onClick = onConfirm,
            )
        },
    )
}

@Composable
@PreviewLightDark
private fun AppSimpleDialogWithIconPreview() {
    GroceryListTheme {
        AppSimpleDialog(
            icon = painterResource(R.drawable.ic_android),
            text = StringValue.StringWrapper("Alert text of this dialog to the user"),
            onConfirm = {},
            onDismiss = {},
            onCancel = {},
        )
    }
}

@Composable
@PreviewLightDark
private fun AppSimpleDialogOkOnlyPreview() {
    GroceryListTheme {
        AppSimpleDialog(
            icon = painterResource(R.drawable.ic_android),
            text = StringValue.StringWrapper("Alert text of this dialog"),
            onConfirm = {},
            onDismiss = {},
        )
    }
}
