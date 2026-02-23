package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import app.grocery.list.commons.compose.theme.BlueColor40
import app.grocery.list.commons.compose.theme.BlueColor60
import app.grocery.list.commons.compose.theme.BrandColor40
import app.grocery.list.commons.compose.theme.BrandColor60
import app.grocery.list.commons.compose.theme.GreenColor40
import app.grocery.list.commons.compose.theme.GreenColor60
import app.grocery.list.commons.compose.theme.MagentaColor40
import app.grocery.list.commons.compose.theme.MagentaColor60

@Immutable
enum class AppColorSchemeProps(
    val demoColor1: Color,
    val demoColor2: Color,
) {
    Yellow(
        demoColor1 = BrandColor40,
        demoColor2 = BrandColor60,
    ),
    Blue(
        demoColor1 = BlueColor40,
        demoColor2 = BlueColor60,
    ),
    Green(
        demoColor1 = GreenColor40,
        demoColor2 = GreenColor60,
    ),
    Magenta(
        demoColor1 = MagentaColor40,
        demoColor2 = MagentaColor60,
    ),
}
