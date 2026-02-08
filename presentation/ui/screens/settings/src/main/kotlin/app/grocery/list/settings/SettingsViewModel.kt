package app.grocery.list.settings

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel(
    assistedFactory = SettingsViewModel.Factory::class,
)
internal class SettingsViewModel @AssistedInject constructor(
    @Assisted
    appVersionName: String,
) : ViewModel(),
    SettingsCallbacks {

    val props = MutableStateFlow(SettingsProps(appVersionName = appVersionName))

    private val events = Channel<Event>(Channel.UNLIMITED)

    override fun onListFormatClick() {
        events.trySend(Event.OnGoToListFormatSettings)
    }

    override fun onContactSupportClick() {
        events.trySend(Event.OnContactSupport)
    }

    override fun onFaqClick() {
        events.trySend(Event.OnFaqClick)
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnGoToListFormatSettings: Event()
        data object OnContactSupport: Event()
        data object OnFaqClick: Event()
    }

    @AssistedFactory
    fun interface Factory {
        fun create(appVersionName: String): SettingsViewModel
    }
}
