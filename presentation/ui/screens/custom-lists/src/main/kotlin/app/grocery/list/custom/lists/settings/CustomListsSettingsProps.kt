package app.grocery.list.custom.lists.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal data class CustomListsSettingsProps(
    val featureEnabled: Boolean,
)

internal class CustomListsSettingsMocks : PreviewParameterProvider<CustomListsSettingsProps> {
    override val values = sequenceOf(
        CustomListsSettingsProps(
            featureEnabled = true,
        ),
        CustomListsSettingsProps(
            featureEnabled = false,
        ),
    )
}
