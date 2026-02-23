package app.grocery.list.commons.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = staticCompositionLocalOf { YellowLightColors }

@Suppress("PropertyName")
@Immutable
data class AppColors(

    val blackOrWhite: Color,
    val whiteOrBlack: Color,
    val inactive1: Color,
    val inactive2: Color,
    val inactive3: Color,

    val brand_00_50: Color,
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
}

val YellowLightColors =
    AppColors(
        blackOrWhite = Color.Black,
        whiteOrBlack = Color.White,

        inactive1 = Inactive1Light,
        inactive2 = Inactive2Light,
        inactive3 = Inactive3Light,

        brand_00_50 = BrandColor00,
        brand_30_40 = BrandColor30,
        brand_40_40 = BrandColor40,
        brand_40_50 = BrandColor40,
        brand_50_50 = BrandColor50,
        brand_60_50 = BrandColor60,
        brand_70_30 = BrandColor70,
        brand_80_20 = BrandColor80,
        brand_90_10 = BrandColor90,
    )

val YellowDarkColors =
    AppColors(
        blackOrWhite = Color.White,
        whiteOrBlack = Color.Black,

        inactive1 = BrandColor20,
        inactive2 = BrandColor30,
        inactive3 = BrandColor80,

        brand_00_50 = BrandColor50,
        brand_30_40 = BrandColor40,
        brand_40_40 = BrandColor40,
        brand_40_50 = BrandColor50,
        brand_50_50 = BrandColor50,
        brand_60_50 = BrandColor50,
        brand_70_30 = BrandColor30,
        brand_80_20 = BrandColor20,
        brand_90_10 = BrandColor10,
    )

val BlueLightColors =
    AppColors(
        blackOrWhite = Color.Black,
        whiteOrBlack = Color.White,

        inactive1 = Inactive1Light,
        inactive2 = Inactive2Light,
        inactive3 = Inactive3Light,

        brand_00_50 = BlueColor00,
        brand_30_40 = BlueColor30,
        brand_40_40 = BlueColor40,
        brand_40_50 = BlueColor40,
        brand_50_50 = BlueColor50,
        brand_60_50 = BlueColor60,
        brand_70_30 = BlueColor70,
        brand_80_20 = BlueColor80,
        brand_90_10 = BlueColor90,
    )

val BlueDarkColors =
    AppColors(
        blackOrWhite = Color.White,
        whiteOrBlack = Color.Black,

        inactive1 = BlueColor20,
        inactive2 = BlueColor30,
        inactive3 = BlueColor80,

        brand_00_50 = BlueColor50,
        brand_30_40 = BlueColor40,
        brand_40_40 = BlueColor40,
        brand_40_50 = BlueColor50,
        brand_50_50 = BlueColor50,
        brand_60_50 = BlueColor50,
        brand_70_30 = BlueColor30,
        brand_80_20 = BlueColor20,
        brand_90_10 = BlueColor10,
    )

val GreenLightColors =
    AppColors(
        blackOrWhite = Color.Black,
        whiteOrBlack = Color.White,

        inactive1 = Inactive1Light,
        inactive2 = Inactive2Light,
        inactive3 = Inactive3Light,

        brand_00_50 = GreenColor00,
        brand_30_40 = GreenColor30,
        brand_40_40 = GreenColor40,
        brand_40_50 = GreenColor40,
        brand_50_50 = GreenColor50,
        brand_60_50 = GreenColor60,
        brand_70_30 = GreenColor70,
        brand_80_20 = GreenColor80,
        brand_90_10 = GreenColor90,
    )

val GreenDarkColors =
    AppColors(
        blackOrWhite = Color.White,
        whiteOrBlack = Color.Black,

        inactive1 = GreenColor20,
        inactive2 = GreenColor30,
        inactive3 = GreenColor80,

        brand_00_50 = GreenColor50,
        brand_30_40 = GreenColor40,
        brand_40_40 = GreenColor40,
        brand_40_50 = GreenColor50,
        brand_50_50 = GreenColor50,
        brand_60_50 = GreenColor50,
        brand_70_30 = GreenColor30,
        brand_80_20 = GreenColor20,
        brand_90_10 = GreenColor10,
    )

val MagentaLightColors =
    AppColors(
        blackOrWhite = Color.Black,
        whiteOrBlack = Color.White,

        inactive1 = Inactive1Light,
        inactive2 = Inactive2Light,
        inactive3 = Inactive3Light,

        brand_00_50 = MagentaColor00,
        brand_30_40 = MagentaColor30,
        brand_40_40 = MagentaColor40,
        brand_40_50 = MagentaColor40,
        brand_50_50 = MagentaColor50,
        brand_60_50 = MagentaColor60,
        brand_70_30 = MagentaColor70,
        brand_80_20 = MagentaColor80,
        brand_90_10 = MagentaColor90,
    )

val MagentaDarkColors =
    AppColors(
        blackOrWhite = Color.White,
        whiteOrBlack = Color.Black,

        inactive1 = MagentaColor20,
        inactive2 = MagentaColor30,
        inactive3 = MagentaColor80,

        brand_00_50 = MagentaColor50,
        brand_30_40 = MagentaColor40,
        brand_40_40 = MagentaColor40,
        brand_40_50 = MagentaColor50,
        brand_50_50 = MagentaColor50,
        brand_60_50 = MagentaColor50,
        brand_70_30 = MagentaColor30,
        brand_80_20 = MagentaColor20,
        brand_90_10 = MagentaColor10,
    )
