package app.grocery.list.custom.product.lists.input.form

import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.color.scheme.ColorSchemeMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.theming.ColorScheme
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CustomListInputFormMapper @Inject constructor(
    private val colorSchemeMapper: ColorSchemeMapper,
) {
    fun transform(params: Params): CustomListInputFormProps {
        val (initial, title, colorScheme) = params
        return CustomListInputFormProps(
            colorScheme = colorSchemeMapper.toPresentation(colorScheme),
            addButtonState = when {
                (initial != null) -> {
                    AppButtonStateProps.Gone
                }
                (title.isBlank()) -> {
                    AppButtonStateProps.Disabled
                }
                else -> {
                    AppButtonStateProps.Normal
                }
            },
            doneButtonState =
                when {
                    (initial == null) -> {
                        AppButtonStateProps.Gone
                    }
                    (initial.title == title && initial.colorScheme == colorScheme) ||
                    (title.isBlank()) -> {
                        AppButtonStateProps.Disabled
                    }
                    else -> {
                        AppButtonStateProps.Normal
                    }
                },
        )
    }

    data class Params(
        val initial: ProductList?,
        val title: String,
        val colorScheme: ColorScheme,
    )
}
