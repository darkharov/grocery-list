package app.grocery.list.commons.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = staticCompositionLocalOf { LightColors }

@Suppress("PropertyName")
@Immutable
data class AppColors(

    val blackOrWhite: Color,
    val whiteOrBlack: Color,
    val inactive1: Color,
    val inactive2: Color,
    val inactive3: Color,

    val brand_00_50: Color,
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
}

val LightColors =
    AppColors(
        blackOrWhite = Color.Black,
        whiteOrBlack = Color.White,

        inactive1 = Inactive1Light,
        inactive2 = Inactive2Light,
        inactive3 = Inactive3Light,

        brand_00_50 = BrandColor00,
        brand_40_40 = BrandColor40,
        brand_40_50 = BrandColor40,
        brand_50_50 = BrandColor50,
        brand_60_50 = BrandColor60,
        brand_70_30 = BrandColor70,
        brand_80_20 = BrandColor80,
        brand_90_10 = BrandColor90,
    )

val DarkColors =
    AppColors(
        blackOrWhite = Color.White,
        whiteOrBlack = Color.Black,

        inactive1 = Inactive1Dark,
        inactive2 = Inactive2Dark,
        inactive3 = Inactive3Dark,

        brand_00_50 = BrandColor50,
        brand_40_40 = BrandColor40,
        brand_40_50 = BrandColor50,
        brand_50_50 = BrandColor50,
        brand_60_50 = BrandColor50,
        brand_70_30 = BrandColor30,
        brand_80_20 = BrandColor20,
        brand_90_10 = BrandColor10,
    )
