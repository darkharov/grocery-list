package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.elements.button.AppButtonAdd
import app.grocery.list.commons.compose.elements.button.AppButtonDone
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun AppAddAndDoneButtonPanel(
    addButtonState: AppButtonStateProps,
    doneButtonState: AppButtonStateProps,
    onAddClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AppButtonAdd(
            onClick = {
                onAddClick()
            },
            modifier = Modifier
                .weight(1f),
            state = addButtonState,
        )
        AppButtonDone(
            onClick = {
                onDoneClick()
            },
            modifier = Modifier
                .weight(1f),
            state = doneButtonState,
        )
    }
}

@Composable
@PreviewLightDark
private fun AppAddAndDoneButtonPanelPreview() {
    GroceryListTheme {
        AppAddAndDoneButtonPanel(
            addButtonState = AppButtonStateProps.Normal,
            doneButtonState = AppButtonStateProps.Normal,
            onAddClick = {},
            onDoneClick = {},
        )
    }
}
