package app.grocery.list.commons.compose.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF_191405)
private val DarkSurface = Color(0xFF_665114)
private val PrimaryDark = Color(0xFF_ffbf01)

private val Primary = Color(0xFF_ffcc34)
private val LightSurface = Color(0xFF_ffd65c)
private val LightBackground = Color(0XFF_fff4d6)

val PositiveActionColor = Color(0xFF_b8e52e)
val NegativeActionColor = Color(0xFF_ff855c)

internal val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    primary = PrimaryDark,
    onPrimary = Color.Black,
    surface = DarkSurface,
    onSurface = Color.White,
)

internal val LightColorScheme = lightColorScheme(
    background = LightBackground,
    primary = Primary,
    onPrimary = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
)
