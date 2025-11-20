package app.grocery.list.clear.notifications.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ClearNotificationsReminderViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel(),
    ClearNotificationsReminderCallbacks {

    val props = settingsRepository
        .clearNotificationsReminderEnabled
        .observe()
        .map { reminderEnabled ->
            ClearNotificationsReminderProps(
                doNotShowAgain = !(reminderEnabled),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val events = Channel<Event>(capacity = Channel.UNLIMITED)

    override fun onDoNotShowAgainCheckedChange(newValue: Boolean) {
        viewModelScope.launch {
            settingsRepository
                .clearNotificationsReminderEnabled
                .set(!(newValue))
        }
    }

    override fun onNext() {
        events.trySend(Event.Next)
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        Next,
    }
}
