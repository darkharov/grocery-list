package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.toolbar.elements.icon.AppToolbarIconProps
import app.grocery.list.commons.compose.values.StringValue

@Immutable
data class AppToolbarProps(
    val title: StringValue,
    val counter: Int?,
    val mightHaveEmoji: Boolean,
    val icons: Icons,
    val progress: Boolean,
) {
    @Immutable
    data class Icons(
        val leading: AppToolbarIconProps.Leading?,
        val trailing: AppToolbarIconProps.Trailing?,
    )

    val noDecorations
        get() =
            !(mightHaveEmoji) &&
            (counter == null)
}

class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps> {
    override val values = sequenceOf(
        AppToolbarProps(
            title = StringValue.ResId(R.string.grocery_list),
            counter = 11,
            mightHaveEmoji = true,
            icons = AppToolbarProps.Icons(
                leading = AppToolbarIconProps.Up,
                trailing = AppToolbarIconProps.Settings,
            ),
            progress = true,
        ),
    )
}
