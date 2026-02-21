package app.grocery.list.commons.compose.elements

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppRadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    RadioButton(
        selected = selected,
        onClick = null, // copied from https://developer.android.com/develop/ui/compose/components/radio-button#create-basic
        modifier = modifier,
        colors = RadioButtonDefaults.colors(
            selectedColor = LocalAppColors.current.brand_50_50,
            unselectedColor = LocalAppColors.current.inactive3,
        ),
    )
}

@Composable
@PreviewLightDark
private fun AppRadioButtonPreview() {
    GroceryListTheme {
        AppRadioButton(
            selected = true,
            modifier = Modifier,
        )
    }
}
