package app.grocery.list.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import javax.inject.Inject
import javax.inject.Singleton

@Composable
fun GroceryListTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        isSystemInDarkTheme() -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}

@Composable
private fun GroceryListTheme(
    emojiProvider: EmojiProviderImpl,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalEmojiProvider provides emojiProvider,
    ) {
        GroceryListTheme(content)
    }
}

@Singleton
class ThemeUtil @Inject constructor(
    private val emojiProvider: EmojiProviderImpl,
) {
    @Composable
    fun GroceryListTheme(
        content: @Composable () -> Unit
    ) {
        GroceryListTheme(
            emojiProvider = emojiProvider,
            content = content,
        )
    }
}
