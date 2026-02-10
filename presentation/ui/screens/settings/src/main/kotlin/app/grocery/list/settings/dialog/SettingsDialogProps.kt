package app.grocery.list.settings.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal sealed class SettingsDialogProps {

    @Immutable
    data object BrowserNotFound: SettingsDialogProps()
}

internal class SettingsDialogMocks : PreviewParameterProvider<SettingsDialogProps> {
    override val values = sequenceOf(
        SettingsDialogProps.BrowserNotFound,
    )
}
