package app.grocery.list.commons.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps

val LocalAppColors = staticCompositionLocalOf { AppColorSchemeProps.Yellow.lightColors }

@Suppress("PropertyName")
@Immutable
data class AppColors(

    val blackOrWhite: Color,
    val whiteOrBlack: Color,
    val inactive1: Color,
    val inactive2: Color,
    val inactive3: Color,

    val brand_00_50: Color,
    val brand_20_80: Color,
    val brand_30_40: Color,
    val brand_40_40: Color,
    val brand_40_50: Color,
    val brand_50_50: Color,
    val brand_60_50: Color,
    val brand_70_30: Color,
    val brand_80_20: Color,
    val brand_90_10: Color,

) {
    val border = brand_50_50
    val background = brand_90_10

    companion object {

        fun light(colorScheme: AppColorSchemeProps) =
            AppColors(
                blackOrWhite = Color.Black,
                whiteOrBlack = Color.White,
                inactive1 = Inactive1Light,
                inactive2 = Inactive2Light,
                inactive3 = Inactive3Light,
                brand_00_50 = colorScheme.color00,
                brand_20_80 = colorScheme.color20,
                brand_30_40 = colorScheme.color30,
                brand_40_40 = colorScheme.color40,
                brand_40_50 = colorScheme.color40,
                brand_50_50 = colorScheme.color50,
                brand_60_50 = colorScheme.color60,
                brand_70_30 = colorScheme.color70,
                brand_80_20 = colorScheme.color80,
                brand_90_10 = colorScheme.color90,
            )

        fun dark(palette: AppColorSchemeProps) =
            AppColors(
                blackOrWhite = Color.White,
                whiteOrBlack = Color.Black,

                inactive1 = palette.color20,
                inactive2 = palette.color30,
                inactive3 = palette.color80,

                brand_00_50 = palette.color50,
                brand_20_80 = palette.color80,
                brand_30_40 = palette.color40,
                brand_40_40 = palette.color40,
                brand_40_50 = palette.color50,
                brand_50_50 = palette.color50,
                brand_60_50 = palette.color50,
                brand_70_30 = palette.color30,
                brand_80_20 = palette.color20,
                brand_90_10 = palette.color10,
            )
    }
}

val Inactive1Light = Color(0xFFF0F0F0)
val Inactive2Light = Color(0xFFA0A0A0)
val Inactive3Light = Color(0xFF424242)

val PositiveActionColor = Color(0xFF_b8e52e)
val NegativeActionColor = Color(0xFF_ff855c)
