package app.grocery.list.main.activity.ui.content

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.elements.color.scheme.AppColorSchemeProps
import app.grocery.list.commons.compose.elements.toolbar.AppToolbarProps

@Immutable
data class AppContentProps(
    val toolbar: AppToolbarProps,
    val colorScheme: AppColorSchemeProps,
)
