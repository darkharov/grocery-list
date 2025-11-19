package app.grocery.list.clear.notifications.reminder

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal data class ClearNotificationsReminderProps(
    val progress: Boolean = false,
    val doNotShowAgain: Boolean = false,
)

internal class ClearNotificationsReminderMock : PreviewParameterProvider<ClearNotificationsReminderProps?> {

    override val values = sequenceOf(
        null,
        ClearNotificationsReminderProps(
            progress = true,
            doNotShowAgain = true,
        ),
    )
}
