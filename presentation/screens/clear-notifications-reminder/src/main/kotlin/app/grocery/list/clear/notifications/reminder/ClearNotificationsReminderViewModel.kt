package app.grocery.list.clear.notifications.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class ClearNotificationsReminderViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel(),
    ClearNotificationsReminderCallbacks {

    val props = MutableStateFlow(ClearNotificationsReminderProps())
    private val events = Channel<Event>(capacity = Channel.UNLIMITED)

    private val doNotShowAgainChannel = Channel<Boolean>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            for (doNotShowAgain in doNotShowAgainChannel) {
                settingsRepository.clearNotificationsReminderEnabled.set(!(doNotShowAgain))
                props.update { it.copy(progress = false) }
            }
        }
    }

    override fun onDoNotShowAgainCheckedChange(newValue: Boolean) {
        props.update { it.copy(progress = true, doNotShowAgain = newValue) }
        doNotShowAgainChannel.trySend(newValue)
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
