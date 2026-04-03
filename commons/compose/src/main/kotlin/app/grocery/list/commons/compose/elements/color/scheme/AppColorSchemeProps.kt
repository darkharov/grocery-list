package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import app.grocery.list.commons.compose.theme.AppColors

@Immutable
enum class AppColorSchemeProps(
    val color00: Color,
    val color10: Color,
    val color20: Color,
    val color30: Color,
    val color40: Color,
    val color50: Color,
    val color60: Color,
    val color70: Color,
    val color80: Color,
    val color90: Color,
) {
    Yellow(
        color00 = Color(0xFF_000000),
        color10 = Color(0xFF_191300),
        color20 = Color(0xFF_332600),
        color30 = Color(0xFF_664c00),
        color40 = Color(0xFF_b48700),
        color50 = Color(0xFF_ffbf01),
        color60 = Color(0xFF_ffcc34),
        color70 = Color(0xFF_ffd65c),
        color80 = Color(0xFF_fff4d6),
        color90 = Color(0XFF_fffdfa),
    ),
    Blue(
        color00 = Color(0xFF_000000),
        color10 = Color(0xFF_001319),
        color20 = Color(0xFF_002633),
        color30 = Color(0xFF_004c66),
        color40 = Color(0xFF_0087b4),
        color50 = Color(0xFF_01c0ff),
        color60 = Color(0xFF_34cdff),
        color70 = Color(0xFF_5cd7ff),
        color80 = Color(0xFF_d6f5ff),
        color90 = Color(0XFF_fafeff),
    ),
    Green(
        color00 = Color(0xFF_000000),
        color10 = Color(0xFF_0f1302),
        color20 = Color(0xFF_1f2705),
        color30 = Color(0xFF_3e4f0a),
        color40 = Color(0xFF_678310),
        color50 = Color(0xFF_9cc719),
        color60 = Color(0xFF_b8e52e),
        color70 = Color(0xFF_c6ea57),
        color80 = Color(0xFF_f0f9d5),
        color90 = Color(0XFF_fdfefa),
    ),
    Magenta(
        color00 = Color(0xFF_000000),
        color10 = Color(0xFF_331a2c),
        color20 = Color(0xFF_4c2743),
        color30 = Color(0xFF_7f4170),
        color40 = Color(0xFF_ff69da),
        color50 = Color(0xFF_ff82e0),
        color60 = Color(0xFF_ff9be6),
        color70 = Color(0xFF_ffafeb),
        color80 = Color(0xFF_ffebfa),
        color90 = Color(0XFF_fffdfe),
    ),
    ;
    val demoColor1 = color40
    val demoColor2 = color60
    val lightColors = AppColors.light(this)
    val darkColors = AppColors.dark(this)
}
