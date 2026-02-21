package app.grocery.list.commons.compose.elements.switch_

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.Black,
            checkedTrackColor = LocalAppColors.current.brand_60_50,
            uncheckedBorderColor = LocalAppColors.current.inactive2,
            uncheckedThumbColor = LocalAppColors.current.inactive2,
            uncheckedTrackColor = LocalAppColors.current.inactive1,
        ),
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun AppSwitchPreview() {
    GroceryListTheme {
        AppSwitch(
            checked = true,
            onCheckedChange = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun AppSwitchUncheckedPreview() {
    GroceryListTheme {
        AppSwitch(
            checked = false,
            onCheckedChange = {},
        )
    }
}
