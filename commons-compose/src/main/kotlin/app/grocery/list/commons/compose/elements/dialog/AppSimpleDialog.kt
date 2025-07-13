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
import app.grocery.list.commons.compose.elements.AppTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppSimpleDialog(
    text: StringValue,
    onDismiss: () -> Unit,
    icon: Painter? = null,
    confirmButtonText: StringValue = StringValue.ResId(android.R.string.ok),
    onConfirm: () -> Unit = {},
    onCancel: (() -> Unit)? = null,
) {
    AppBaseDialog(
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
            text = StringValue.StringWrapper("Alert text of this dialog"),
            onDismiss = {},
        )
    }
}
