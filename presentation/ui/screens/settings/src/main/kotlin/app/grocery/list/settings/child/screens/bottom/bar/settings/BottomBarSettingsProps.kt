package app.grocery.list.settings.child.screens.bottom.bar.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal data class BottomBarSettingsProps(
    val useIcons: Boolean,
)

internal class BottomBarSettingsMocks : PreviewParameterProvider<BottomBarSettingsProps> {
    override val values = sequenceOf(
        BottomBarSettingsProps(
            useIcons = true,
        ),
        BottomBarSettingsProps(
            useIcons = false,
        ),
    )
}
