package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSimpleDialog(
    onDismiss: () -> Unit,
    icon: Painter? = null,
    title: StringValue? = null,
    text: StringValue? = null,
    additionalContent: (@Composable ColumnScope.() -> Unit)? = null,
    cancelButtonVisible: Boolean = false,
    mainButtonText: StringValue = StringValue.ResId(android.R.string.ok),
    onMainButtonClick: () -> Unit = onDismiss,
) {
    AppBaseDialog(
        onDismiss = onDismiss,
        icon = icon,
        title = title,
        text = text,
        additionalContent = additionalContent,
        buttons = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .align(Alignment.End),
            ) {
                if (cancelButtonVisible) {
                    AppTextButton(
                        text = StringValue.ResId(android.R.string.cancel),
                        onClick = onDismiss,
                    )
                    Spacer(
                        modifier = Modifier
                            .width(APP_DIALOG_BUTTON_PADDING),
                    )
                }
                AppTextButton(
                    text = mainButtonText,
                    onClick = onMainButtonClick,
                )
            }
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
            onMainButtonClick = {},
            onDismiss = {},
            cancelButtonVisible = true,
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
            onMainButtonClick = {},
            onDismiss = {},
        )
    }
}
