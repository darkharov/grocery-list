package app.grocery.list.commons.compose.elements.start.and.end.button.panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun AppStartAndEndButtonPanel(
    startButtonText: StringValue?,
    onStartClick: () -> Unit,
    endButtonText: StringValue?,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
    endButtonEnabled: Boolean = true,
    startButtonEnabled: Boolean = true,
) {
    val buttonHorizontalPadding = 8.dp
    val buttonPaddingValues = PaddingValues(
        horizontal = buttonHorizontalPadding,
        vertical = 12.dp,
    )
    val desiredHorizontalOffset = dimensionResource(R.dimen.margin_16_32_64)
    val finalHorizontalOffset = desiredHorizontalOffset - buttonHorizontalPadding
    val maxWidth = 180.dp
    Row(
        modifier = modifier
            .padding(
                horizontal = finalHorizontalOffset,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (startButtonText != null) {
            AppTextButton(
                text = startButtonText,
                onClick = onStartClick,
                enabled = startButtonEnabled,
                padding = buttonPaddingValues,
                modifier = Modifier
                    .widthIn(max = maxWidth),
            )
        }
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
        if (endButtonText != null) {
            AppTextButton(
                text = endButtonText,
                enabled = endButtonEnabled,
                padding = buttonPaddingValues,
                onClick = onEndClick,
                modifier = Modifier
                    .widthIn(max = maxWidth),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AppStartAndEndButtonPanelPreview() {
    GroceryListTheme {
        AppStartAndEndButtonPanel(

            startButtonText = StringValue.StringWrapper("Start"),
            startButtonEnabled = true,
            onStartClick = {},

            endButtonText = StringValue.StringWrapper("End"),
            endButtonEnabled = false,
            onEndClick = {},

            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppStartAndEndButtonPanelEndOnlyPreview() {
    GroceryListTheme {
        AppStartAndEndButtonPanel(

            startButtonText = null,
            onStartClick = {},

            endButtonText = StringValue.StringWrapper("End"),
            onEndClick = {},

            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun AppStartAndEndButtonPanelStartOnlyPreview() {
    GroceryListTheme {
        AppStartAndEndButtonPanel(

            startButtonText = StringValue.StringWrapper("Start"),
            startButtonEnabled = true,
            onStartClick = {},

            endButtonText = null,
            onEndClick = {},

            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
