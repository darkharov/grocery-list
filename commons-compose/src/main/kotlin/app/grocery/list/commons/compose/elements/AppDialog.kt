package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.values.StringValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDialogScreen(
    text: StringValue,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    confirmButtonText: StringValue = StringValue.ResId(android.R.string.ok),
    onConfirm: () -> Unit = {},
    onCancel: (() -> Unit)? = onDismiss,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .widthIn(
                    min = 280.dp,
                    max = 560.dp,
                ),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Content(
                icon = icon,
                text = text,
                onCancel = onCancel,
                onConfirm = onConfirm,
                confirmButtonText = confirmButtonText,
            )
        }
    }
}

@Composable
private fun Content(
    icon: Painter?,
    text: StringValue,
    onCancel: (() -> Unit)?,
    onConfirm: () -> Unit,
    confirmButtonText: StringValue,
) {
    val commonPadding = 16.dp
    Column(
        modifier = Modifier
            .padding(commonPadding),
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
            )
        }
        Spacer(
            modifier = Modifier
                .height(commonPadding),
        )
        Text(
            text = text.value(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )
        Spacer(
            modifier = Modifier
                .height(commonPadding),
        )
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .align(Alignment.End),
        ) {
            if (onCancel != null) {
                AppTextButton(
                    text = StringValue.ResId(android.R.string.cancel),
                    onClick = onCancel,
                )
            }
            AppTextButton(
                text = confirmButtonText,
                onClick = onConfirm,
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AppDialogWithIconPreview() {
    GroceryListTheme {
        AppDialogScreen(
            icon = painterResource(R.drawable.ic_android),
            text = StringValue.StringWrapper("Alert text of this dialog to the user"),
            onDismiss = {},
            onCancel = {},
            modifier = Modifier,
        )
    }
}

@Composable
@PreviewLightDark
private fun AppDialogOkOnlyPreview(
) {
    GroceryListTheme {
        AppDialogScreen(
            text = StringValue.StringWrapper("Alert text of this dialog"),
            onDismiss = {},
            modifier = Modifier,
        )
    }
}
