package app.grocery.list.commons.compose.elements.button

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
enum class AppButtonStateProps {
    Enabled,
    Disabled,
    Loading,
    ;

    val enabled get() = (this == Enabled)
    val loading get() = (this == Loading)

    companion object {

        @Stable
        fun enabled(enabled: Boolean) =
            if (enabled) {
                Enabled
            } else {
                Disabled
            }

        @Stable
        fun progress(progress: Boolean) =
            if (progress) {
                Loading
            } else {
                Enabled
            }
    }
}

internal class AppButtonStateMocks : PreviewParameterProvider<AppButtonStateProps> {
    override val values = AppButtonStateProps.entries.asSequence()
}
