package app.grocery.list.clear.notifications.reminder

import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Immutable
internal data class ClearNotificationsReminderProps(
    val doNotShowAgain: Boolean,
)

internal class ClearNotificationsReminderMock : PreviewParameterProvider<ClearNotificationsReminderProps?> {

    override val values = sequenceOf(
        null,
        ClearNotificationsReminderProps(
            doNotShowAgain = true,
        ),
    )
}
