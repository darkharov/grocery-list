package app.grocery.list.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import app.grocery.list.commons.compose.LocalToolbarEmojiProvider
import app.grocery.list.commons.compose.ToolbarEmojiProvider
import javax.inject.Inject
import javax.inject.Singleton

@Composable
fun GroceryListTheme(
    content: @Composable () -> Unit,
) {
    val colors = if (isSystemInDarkTheme()) {
        DarkColors
    } else {
        LightColors
    }
    MaterialTheme {
        CompositionLocalProvider(
            LocalTextStyle provides LocalAppTypography.current.plainText,
            LocalAppColors provides colors,
        ) {
            content()
        }
    }
}

@Composable
private fun GroceryListTheme(
    toolbarEmojiProvider: ToolbarEmojiProvider,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalToolbarEmojiProvider provides toolbarEmojiProvider,
    ) {
        GroceryListTheme(content)
    }
}

@Singleton
class ThemeUtil @Inject constructor(
    private val emojiProvider: ToolbarEmojiProvider,
) {
    @Composable
    fun GroceryListTheme(
        content: @Composable () -> Unit,
    ) {
        GroceryListTheme(
            toolbarEmojiProvider = emojiProvider,
            content = content,
        )
    }
}
