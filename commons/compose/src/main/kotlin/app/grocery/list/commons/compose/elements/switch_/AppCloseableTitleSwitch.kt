package app.grocery.list.commons.compose.elements.switch_

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.icon.AppCloseButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppCloseableTitleSwitch(
    checked: Boolean,
    text: StringValue,
    explanation: StringValue?,
    onClose: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        AppCloseButton(
            onClick = {
                onClose()
            },
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    bottom = 2.dp,
                )
                .align(Alignment.End)
        )
        AppTitledSwitch(
            checked = checked,
            text = text,
            explanation = explanation,
            onCheckedChange = onCheckedChange,
        )
    }
}

@PreviewLightDark
@Composable
private fun AppCloseableTitleSwitchPreview() {
    GroceryListTheme {
        AppCloseableTitleSwitch(
            checked = true,
            text = StringValue.StringWrapper("Text"),
            explanation = StringValue.StringWrapper("Explanation"),
            onClose = {},
            onCheckedChange = {},
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
