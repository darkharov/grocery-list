package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import app.grocery.list.commons.compose.theme.AppColors
import app.grocery.list.commons.compose.theme.BlueColor40
import app.grocery.list.commons.compose.theme.BlueColor60
import app.grocery.list.commons.compose.theme.BlueDarkColors
import app.grocery.list.commons.compose.theme.BlueLightColors
import app.grocery.list.commons.compose.theme.BrandColor40
import app.grocery.list.commons.compose.theme.BrandColor60
import app.grocery.list.commons.compose.theme.GreenColor40
import app.grocery.list.commons.compose.theme.GreenColor60
import app.grocery.list.commons.compose.theme.GreenDarkColors
import app.grocery.list.commons.compose.theme.GreenLightColors
import app.grocery.list.commons.compose.theme.MagentaColor40
import app.grocery.list.commons.compose.theme.MagentaColor60
import app.grocery.list.commons.compose.theme.MagentaDarkColors
import app.grocery.list.commons.compose.theme.MagentaLightColors
import app.grocery.list.commons.compose.theme.YellowDarkColors
import app.grocery.list.commons.compose.theme.YellowLightColors

@Immutable
enum class AppColorSchemeProps(
    val demoColor1: Color,
    val demoColor2: Color,
    val lightColors: AppColors,
    val darkColors: AppColors,
) {
    Yellow(
        demoColor1 = BrandColor40,
        demoColor2 = BrandColor60,
        lightColors = YellowLightColors,
        darkColors = YellowDarkColors,
    ),
    Blue(
        demoColor1 = BlueColor40,
        demoColor2 = BlueColor60,
        lightColors = BlueLightColors,
        darkColors = BlueDarkColors,
    ),
    Green(
        demoColor1 = GreenColor40,
        demoColor2 = GreenColor60,
        lightColors = GreenLightColors,
        darkColors = GreenDarkColors,
    ),
    Magenta(
        demoColor1 = MagentaColor40,
        demoColor2 = MagentaColor60,
        lightColors = MagentaLightColors,
        darkColors = MagentaDarkColors,
    ),
    ;
}
