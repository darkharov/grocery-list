package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.grocery.list.commons.compose.theme.LocalAppColors

@Composable
fun AppPreloader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(LocalAppColors.current.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AppCircularProgressIndicator()
    }
}

@Composable
inline fun <reified T : Any> AppPreloaderOrContent(
    props: T?,
    content: (props: T) -> Unit,
) {
    if (props != null) {
        content(props)
    } else {
        AppPreloader()
    }
}
