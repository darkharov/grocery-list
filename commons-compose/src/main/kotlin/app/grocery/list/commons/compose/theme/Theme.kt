package app.grocery.list.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import app.grocery.list.commons.compose.EmojiProvider
import app.grocery.list.commons.compose.EmojiProviderImpl
import app.grocery.list.commons.compose.LocalEmojiProvider
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
        content = {
            CompositionLocalProvider(LocalTextStyle provides LocalAppTypography.current.plainText) {
                content()
            }
        },
    )
}

@Composable
private fun GroceryListTheme(
    emojiProvider: EmojiProvider,
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
