package app.grocery.list.custom.lists

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.grocery.list.commons.compose.theme.GroceryListTheme

@Composable
fun CustomListsScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Text("Custom Lists")
    }
}

@Preview
@Composable
private fun CustomListScreenPreview() {
    GroceryListTheme {
        CustomListsScreen()
    }
}
