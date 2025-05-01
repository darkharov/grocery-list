package app.grocery.list.commons.compose.theme.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.grocery.list.commons.compose.R

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.app_name),
    titleTrailingContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppToolbar(
                title = title,
                titleTrailingContent = titleTrailingContent,
            )
        },
        content = content,
    )
}
