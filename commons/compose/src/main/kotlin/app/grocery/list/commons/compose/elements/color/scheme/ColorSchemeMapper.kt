package app.grocery.list.commons.compose.elements.color.scheme

import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorSchemeMapper @Inject constructor() {

    fun toPresentation(colorScheme: ColorScheme) =
        when (colorScheme) {
            ColorScheme.Yellow -> {
                AppColorSchemeProps.Yellow
            }
            ColorScheme.Blue -> {
                AppColorSchemeProps.Blue
            }
            ColorScheme.Green -> {
                AppColorSchemeProps.Green
            }
            ColorScheme.Magenta -> {
                AppColorSchemeProps.Magenta
            }
        }

    fun toDomain(props: AppColorSchemeProps) =
        when (props) {
            AppColorSchemeProps.Yellow -> {
                ColorScheme.Yellow
            }
            AppColorSchemeProps.Blue -> {
                ColorScheme.Blue
            }
            AppColorSchemeProps.Green -> {
                ColorScheme.Green
            }
            AppColorSchemeProps.Magenta -> {
                ColorScheme.Magenta
            }
        }
}
