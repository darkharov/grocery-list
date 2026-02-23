package app.grocery.list.custom.product.lists.input.form

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps

@Immutable
internal data class CustomListInputFormProps(
    val colorScheme: AppColorSchemeProps,
    val addButtonState: AppButtonStateProps,
    val doneButtonState: AppButtonStateProps,
)

internal class CustomListInputFormMock : PreviewParameterProvider<CustomListInputFormProps?> {

    private val prototype = CustomListInputFormProps(
        colorScheme = AppColorSchemeProps.Yellow,
        addButtonState = AppButtonStateProps.Normal,
        doneButtonState = AppButtonStateProps.Gone,
    )

    override val values = sequenceOf(null, prototype)
}
