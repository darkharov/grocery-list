package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.icon.AppCloseButton
import app.grocery.list.commons.compose.elements.button.text.AppUnderlinedTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppQuestion(
    text: StringValue,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        AppUnderlinedTextButton(
            text = text,
            onClick = onClick,
            modifier = Modifier
                .padding(horizontal = 52.dp)
        )
        AppCloseButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.CenterEnd),
        )
    }
}

@Composable
@PreviewLightDark
private fun AppQuestionPreview() {
    GroceryListTheme {
        AppQuestion(
            text = StringValue.StringWrapper("Text"),
            onClick = {},
            onClose = {},
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
