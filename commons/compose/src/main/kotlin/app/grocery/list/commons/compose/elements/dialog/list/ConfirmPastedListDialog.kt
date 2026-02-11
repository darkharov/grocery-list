package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.drawGradient
import app.grocery.list.commons.compose.elements.button.text.AppTextButton2
import app.grocery.list.commons.compose.elements.dialog.APP_DIALOG_PADDING
import app.grocery.list.commons.compose.elements.dialog.AppBaseDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun ConfirmPastedListDialog(
    props: ConfirmPastedListDialogProps,
    callbacks: ConfirmPastedListDialogCallbacks,
) {
    AppBaseDialog(
        icon = rememberVectorPainter(AppIcons.paste),
        text = props.title,
        textStyle = LocalAppTypography.current.dialogTitle,
        onDismiss = {
            callbacks.onDialogDismiss()
        },
        additionalContent = {
            val offset = 48.dp
            val state = rememberScrollState()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .heightIn(max = 4 * offset)
                    .fillMaxWidth()
                    .drawGradient(
                        direction = AppGradientDirection.Upward,
                        color = MaterialTheme.colorScheme.surface,
                        height = offset,
                        visible = state.canScrollForward,
                    )
                    .drawGradient(
                        direction = AppGradientDirection.Downward,
                        color = MaterialTheme.colorScheme.surface,
                        height = offset,
                        visible = state.canScrollBackward,
                    )
                    .verticalScroll(state)
            ) {
                Text(
                    text = props.text,
                    modifier = Modifier
                        .widthIn(max = 180.dp),
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(4.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(APP_DIALOG_PADDING),
            ) {
                AppTextButton2(
                    text = StringValue.ResId(android.R.string.cancel),
                    onClick = {
                        callbacks.onDialogDismiss()
                    },
                )
                AppTextButton2(
                    text = StringValue.ResId(R.string.add),
                    onClick = {
                        callbacks.onPasteProductsConfirmed(props.productList)
                    },
                )
            }
        },
    )
}

@Preview
@Composable
private fun ConfirmPastedListDialogPreview() {
    GroceryListTheme {
        ConfirmPastedListDialog(
            props = ConfirmPastedListMock.prototype,
            callbacks = ConfirmPastedListDialogCallbacksMock,
        )
    }
}
