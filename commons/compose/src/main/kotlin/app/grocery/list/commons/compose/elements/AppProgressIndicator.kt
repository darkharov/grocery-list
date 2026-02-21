package app.grocery.list.commons.compose.elements

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = LocalAppColors.current.brand_50_50,
        strokeWidth = strokeWidth,
    )
}

@PreviewLightDark
@Composable
private fun AppCircularProgressIndicatorPreview() {
    GroceryListTheme {
        AppCircularProgressIndicator()
    }
}
