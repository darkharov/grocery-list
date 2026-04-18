package app.grocery.list.commons.compose.elements.dialog.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.drawGradient
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun ConfirmPastedListDialog(
    props: ConfirmPastedListDialogProps,
    callbacks: ConfirmPastedListDialogCallbacks,
) {
    AppSimpleDialog(
        icon = rememberVectorPainter(AppIcons.paste),
        title = props.title,
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
                        color = LocalAppColors.current.brand_80_20,
                        height = offset,
                        visible = state.canScrollForward,
                    )
                    .drawGradient(
                        direction = AppGradientDirection.Downward,
                        color = LocalAppColors.current.brand_80_20,
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
        },
        onDismiss = {
            callbacks.onDialogDismiss()
        },
        mainButtonText = StringValue.ResId(R.string.add),
        onMainButtonClick = {
            callbacks.onPasteProductsConfirmed(props.productList)
        },
    )
}

@PreviewLightDark
@Composable
private fun ConfirmPastedListDialogPreview() {
    GroceryListTheme {
        ConfirmPastedListDialog(
            props = ConfirmPastedListMock.prototype,
            callbacks = ConfirmPastedListDialogCallbacksMock,
        )
    }
}
