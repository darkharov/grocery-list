package app.grocery.list.commons.compose.elements

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
            )
        },
        onClick = onClick,
        modifier = modifier,
        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        colors = MenuItemColors(
            textColor = LocalAppColors.current.blackOrWhite,
            leadingIconColor = LocalAppColors.current.brand_50_50,
            trailingIconColor = LocalAppColors.current.brand_50_50,
            disabledTextColor = LocalAppColors.current.inactive2,
            disabledLeadingIconColor = LocalAppColors.current.inactive2,
            disabledTrailingIconColor = LocalAppColors.current.inactive2,
        ),
    )
}

@Preview
@Composable
private fun AppDropdownMenuItemPreview() {
    GroceryListTheme {
        AppDropdownMenuItem(
            text = "Text",
            onClick = {},
        )
    }
}
