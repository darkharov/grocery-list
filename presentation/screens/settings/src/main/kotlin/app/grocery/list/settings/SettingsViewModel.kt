package app.grocery.list.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

@HiltViewModel
internal class SettingsViewModel @Inject constructor() : ViewModel(), SettingsCallbacks {

    private val events = Channel<Event>(Channel.UNLIMITED)

    override fun onListFormatClick() {
        events.trySend(Event.OnGoToListFormatSettings)
    }

    override fun onContactSupportClick() {
        events.trySend(Event.OnContactSupport)
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnGoToListFormatSettings: Event()
        data object OnContactSupport: Event()
    }
}
