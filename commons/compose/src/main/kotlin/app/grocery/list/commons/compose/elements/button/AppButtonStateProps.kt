package app.grocery.list.commons.compose.elements.button

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
enum class AppButtonStateProps {
    Gone,
    Normal,
    Disabled,
    Loading,
    ;

    companion object {

        @Stable
        fun enabled(enabled: Boolean) =
            if (enabled) {
                Normal
            } else {
                Disabled
            }
    }
}

internal class AppButtonStateMocks : PreviewParameterProvider<AppButtonStateProps> {
    override val values = AppButtonStateProps.entries.asSequence()
}
