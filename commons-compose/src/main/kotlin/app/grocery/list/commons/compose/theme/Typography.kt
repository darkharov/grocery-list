package app.grocery.list.commons.compose.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import app.grocery.list.commons.compose.R

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

private val Inter = FontFamily(
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_regular, FontWeight.Normal),
)

private val RobotoCondenced = FontFamily(
    Font(R.font.roboto_condensed_bold, FontWeight.Bold)
)

@Immutable
class AppTypography {

    val plainText = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    )

    val button = TextStyle(
        fontFamily = Inter,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
    )

    val textButton = TextStyle(
        fontFamily = Inter,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
    )

    val toolbarTitle = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    )

    val header = TextStyle(
        fontFamily = RobotoCondenced,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp,
        fontWeight = FontWeight.Bold,
    )
}
