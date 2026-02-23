package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

val APP_DIALOG_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBaseDialog(
    icon: Painter? = null,
    text: StringValue,
    onDismiss: () -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    additionalContent: @Composable (ColumnScope.() -> Unit),
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
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
            color = LocalAppColors.current.brand_80_20,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(APP_DIALOG_PADDING),
                horizontalAlignment = Alignment.End,
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = LocalAppColors.current.brand_40_40,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(APP_DIALOG_PADDING),
                )
                Text(
                    text = text.value(),
                    color = LocalAppColors.current.blackOrWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = textStyle,
                )
                Spacer(
                    modifier = Modifier
                        .height(APP_DIALOG_PADDING),
                )
                additionalContent()
            }
        }
    }
}

@Composable
internal fun AppTextWithButtonsRowDialog(
    icon: Painter? = null,
    text: StringValue,
    onDismiss: () -> Unit,
    buttons: @Composable (RowScope.() -> Unit),
) {
    AppBaseDialog(
        icon = icon,
        onDismiss = onDismiss,
        text = text,
        additionalContent = {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .align(Alignment.End),
            ) {
                buttons()
            }
        },
    )
}
