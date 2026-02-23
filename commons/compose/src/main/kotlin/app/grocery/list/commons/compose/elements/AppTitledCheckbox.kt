package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppTitledCheckbox(
    title: String,
    checked: Boolean,
    onCheckedChange: (newValue: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onCheckedChange(!(checked))
            }
            .padding(vertical = 8.dp)
            .padding(start = 8.dp, end = 12.dp),
    ) {
        val checkboxSize = 20.dp
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(end = 12.dp + checkboxSize),
        )
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = LocalAppColors.current.brand_60_50,
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .requiredSize(checkboxSize),
        )
    }
}

@PreviewLightDark
@Composable
private fun AppTitledCheckboxPreview() {
    GroceryListTheme {
        AppTitledCheckbox(
            title = "Text",
            checked = true,
            onCheckedChange = {},
            modifier = Modifier
                .background(LocalAppColors.current.background)
                .padding(16.dp),
        )
    }
}
