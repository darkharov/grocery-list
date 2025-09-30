package app.grocery.list.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

private val BrandColor10 = Color(0xFF_191300)
private val BrandColor20 = Color(0xFF_332600)
private val BrandColor30 = Color(0xFF_664c00)
private val BrandColor40 = Color(0xFF_b48700)
private val BrandColor50 = Color(0xFF_ffbf01)
private val BrandColor60 = Color(0xFF_ffcc34)
private val BrandColor70 = Color(0xFF_ffd65c)
private val BrandColor80 = Color(0xFF_fff4d6)
private val BrandColor90 = Color(0XFF_fffdfa)

val DisabledLight = Color(0xFFF0F0F0)
val Disabled2Light = Color(0xFFA0A0A0)

val DisabledDark = Color(0xFF212121)
val Disabled2Dark = Color(0xFF424242)

val PositiveActionColor = Color(0xFF_b8e52e)
val NegativeActionColor = Color(0xFF_ff855c)

@Suppress("UnusedReceiverParameter")
val ColorScheme.blackOrWhite
    @Composable
    @ReadOnlyComposable
    get() =
        if (!(isSystemInDarkTheme())) {
            Color.Black
        } else {
            Color.White
        }

internal val DarkColorScheme = darkColorScheme(
    background = BrandColor10,
    primary = BrandColor50,
    onPrimary = Color.Black,
    secondary = BrandColor40,
    surface = BrandColor20,
    onSurface = Color.White,
    inverseSurface = BrandColor30,
    inverseOnSurface = BrandColor50,
    outline = Disabled2Dark,
    surfaceContainerHighest = DisabledDark,
    inversePrimary = BrandColor80,
)

internal val LightColorScheme = lightColorScheme(
    background = BrandColor90,
    primary = BrandColor60,
    onPrimary = Color.Black,
    secondary = BrandColor40,
    surface = BrandColor80,
    onSurface = Color.Black,
    inverseSurface = BrandColor70,
    inverseOnSurface = Color.Black,
    outline = Disabled2Light,
    surfaceContainerHighest = DisabledLight,
    inversePrimary = BrandColor30,
)
