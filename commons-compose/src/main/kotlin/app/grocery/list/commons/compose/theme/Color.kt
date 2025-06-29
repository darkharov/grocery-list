package app.grocery.list.commons.compose.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val DarkBackground = Color(0xFF_191300)
private val DarkSurface = Color(0xFF_332600)
private val DarkInverseSurface = Color(0xFF_664c00)
private val DarkPrimary = Color(0xFF_ffbf01)

private val Secondary = Color(0xFF_b48700)

private val LightPrimary = Color(0xFF_ffcc34)
private val LightInverseSurface = Color(0xFF_ffd65c)
private val LightSurface = Color(0xFF_fff4d6)
private val LightBackground = Color(0XFF_fffdfa)

val PositiveActionColor = Color(0xFF_b8e52e)
val NegativeActionColor = Color(0xFF_ff855c)

internal val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    primary = DarkPrimary,
    onPrimary = Color.Black,
    secondary = Secondary,
    surface = DarkSurface,
    onSurface = Color.White,
    inverseSurface = DarkInverseSurface,
)

internal val LightColorScheme = lightColorScheme(
    background = LightBackground,
    primary = LightPrimary,
    onPrimary = Color.Black,
    secondary = Secondary,
    surface = LightSurface,
    onSurface = Color.Black,
    inverseSurface = LightInverseSurface,
)
