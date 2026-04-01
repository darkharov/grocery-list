package app.grocery.list.custom.product.lists.picker.dialog

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import app.grocery.list.commons.compose.elements.dialog.APP_DIALOG_PADDING
import app.grocery.list.commons.compose.elements.dialog.AppSimpleDialog
import app.grocery.list.commons.compose.elements.dialog.AppTwoOptionsDialog
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppTypography
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.custom.product.lists.R

@Composable
internal fun ProductListPickerOptionalDialog(
    props: ProductListPickerDialogProps?,
    callbacks: ProductListPickerDialogCallbacks,
) {
    when (props) {
        is ProductListPickerDialogProps.CustomListDeletionConfirmation -> {
            AppTwoOptionsDialog(
                icon = rememberVectorPainter(AppIcons.delete),
                title = StringValue.StringWrapper(props.title),
                text = StringValue.ResId(R.string.delete_product_list_confirmation),
                onDismiss = {
                    callbacks.onDeletionRejected(props.id)
                },
                firstOption = StringValue.ResId(R.string.delete),
                isFirstOptionSensitive = true,
                onFirstOption = {
                    callbacks.onDeletionConfirmed(props.id)
                },
            )
        }
        is ProductListPickerDialogProps.HowToRenameOrDeleteCustomList -> {
            AppSimpleDialog(
                icon = rememberVectorPainter(AppIcons.edit),
                onDismiss = {
                    callbacks.onQuestionDialogClose()
                },
                additionalContent = {
                    val textAlign = TextAlign.Center
                    Text(
                        text = buildAnnotatedString {
                            val answer = stringResource(R.string.how_to_edit_items_answer)
                            append(answer)

                            val bold = SpanStyle(fontWeight = FontWeight.Bold)

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
                        style = LocalAppTypography.current.plainText,
                        textAlign = textAlign,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    Spacer(
                        modifier = Modifier
                            .height(APP_DIALOG_PADDING),
                    )
                    Text(
                        text = stringResource(R.string.default_list_is_uneditable_notice),
                        textAlign = textAlign,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                },
            )
        }
        null -> {
            // nothing to show
        }
    }
}

@Composable
@PreviewLightDark
private fun ProductListPickerOptionalDialogPreview(
    @PreviewParameter(
        provider = ProductListPickerDialogMocks::class,
    )
    props: ProductListPickerDialogProps,
) {
    GroceryListTheme {
        ProductListPickerOptionalDialog(
            props = props,
            callbacks = ProductListPickerDialogCallbacksMock,
        )
    }
}
