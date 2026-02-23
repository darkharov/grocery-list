package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppColorSchemePicker(
    selection: AppColorSchemeProps,
    onSelectionChange: (newSelection: AppColorSchemeProps) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.color_scheme),
            color = LocalAppColors.current.blackOrWhite,
            fontSize = 14.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .alpha(0.82f)
                .padding(vertical = 8.dp),
        ) {
            AppColorSchemeProps.entries.forEach { item ->
                AppColorSchemePickerItem(
                    value = item,
                    selected = (selection == item),
                    onSelect = onSelectionChange,
                )
            }
        }
    }
}

@Suppress("AssignedValueIsNeverRead")
@Preview
@Composable
private fun AppColorSchemePickerPreview() {
    GroceryListTheme {
        var selection by remember { mutableStateOf(AppColorSchemeProps.Yellow) }
        AppColorSchemePicker(
            selection = selection,
            onSelectionChange = {
                selection = it
            },
        )
    }
}
