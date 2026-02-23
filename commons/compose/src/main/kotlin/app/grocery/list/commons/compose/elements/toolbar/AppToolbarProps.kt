package app.grocery.list.commons.compose.elements.toolbar

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.values.StringValue

@Immutable
data class AppToolbarProps(
    val title: StringValue = StringValue.ResId(R.string.app_name),
    val counter: Int? = null,
    val mightHaveEmoji: Boolean = false,
    val icons: Icons = Icons(
        leading = AppToolbarIconProps.Up,
        trailing = null,
    ),
    val progress: Boolean = false,
) {
    @Immutable
    data class Icons(
        val leading: AppToolbarIconProps.Leading?,
        val trailing: AppToolbarIconProps.Trailing?,
    )
}

internal class AppToolbarMocks : PreviewParameterProvider<AppToolbarProps> {
    override val values = sequenceOf(
        AppToolbarProps(
            title = StringValue.ResId(R.string.grocery_list),
            counter = 11,
            mightHaveEmoji = true,
            icons = AppToolbarProps.Icons(
                leading = AppToolbarIconProps.Up,
                trailing = AppToolbarIconProps.Settings,
            ),
        ),
        AppToolbarProps(
            title = StringValue.ResId(R.string.settings),
        ),
    )
}
