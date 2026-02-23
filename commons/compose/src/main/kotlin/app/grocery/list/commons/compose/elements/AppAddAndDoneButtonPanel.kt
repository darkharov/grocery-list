package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets
                    .systemBars
                    .only(WindowInsetsSides.Bottom)
            ),
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
