package app.grocery.list.commons.compose.elements.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.OnDialogDismiss
import app.grocery.list.commons.compose.OnDialogDismissMock
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.commons.compose.values.value

@Composable
fun AppHowToEditListItemsDialog(
    callbacks: OnDialogDismiss,
    additionalText: StringValue? = null,
) {
    AppSimpleDialog(
        icon = rememberVectorPainter(AppIcons.edit),
        onDismiss = {
            callbacks.onDialogDismiss()
        },
        additionalContent = {
            val textAlign = TextAlign.Center
            val color = LocalAppColors.current.brand_20_80
            Text(
                text = buildAnnotatedString {

                    val answer = stringResource(R.string.how_to_edit_items_answer)
                    append(answer)

                    val bold = SpanStyle(
                        fontWeight = FontWeight.ExtraBold,
                    )

                    val highlight1 = stringResource(R.string.how_to_edit_items_answer_highlight_1)
                    val highlight1Start = answer.indexOf(highlight1)

                    val highlight2 = stringResource(R.string.how_to_edit_items_answer_highlight_2)
                    val highlight2Start = answer.indexOf(highlight2)

                    addStyle(
                        style = bold,
                        start = highlight1Start,
                        end = highlight1Start + highlight1.length,
                    )
                    addStyle(
                        style = bold,
                        start = highlight2Start,
                        end = highlight2Start + highlight2.length,
                    )
                },
                color = color,
                style = LocalAppTypography.current.plainText,
                textAlign = textAlign,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            if (additionalText != null) {
                Spacer(
                    modifier = Modifier
                        .height(APP_DIALOG_PADDING),
                )
                Text(
                    text = additionalText.value(),
                    textAlign = textAlign,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        },
    )
}

@Composable
@PreviewLightDark
private fun AppHowToEditListItemsDialogPreview() {
    GroceryListTheme {
        AppHowToEditListItemsDialog(
            callbacks = OnDialogDismissMock,
            additionalText = StringValue.StringWrapper("Additional text")
        )
    }
}
