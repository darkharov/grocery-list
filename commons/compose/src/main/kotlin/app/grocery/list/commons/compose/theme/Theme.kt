package app.grocery.list.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps

@Composable
fun GroceryListTheme(
    colorScheme: AppColorSchemeProps = AppColorSchemeProps.Yellow,
    content: @Composable () -> Unit,
) {
    val colors = if (isSystemInDarkTheme()) {
        colorScheme.darkColors
    } else {
        colorScheme.lightColors
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
