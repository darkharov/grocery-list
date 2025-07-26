package app.grocery.list.assembly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.assembly.ui.content.AppEvent
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.settings.Settings
import app.grocery.list.storage.value.kotlin.get
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel() {

    private val appEvents = Channel<AppEvent>(capacity = Channel.UNLIMITED)

    val numberOfAddedProducts =
        repository
            .numberOfAddedProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val productList =
        repository
            .categorizedProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val itemInNotificationMode: StateFlow<Settings.ItemInNotificationMode?> =
        repository
            .itemInNotificationMode()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val progress = MutableStateFlow(false)

    fun appEvents(): ReceiveChannel<AppEvent> =
        appEvents

    fun notifyPushNotificationsGranted() {
        viewModelScope.launch(Dispatchers.IO) {
            progress.value = true
            val enabled = repository.clearNotificationsReminderEnabled.get()
            val event = AppEvent.PushNotificationsGranted(
                clearNotificationsReminderEnabled = enabled
            )
            appEvents.trySend(event)
            progress.value = false
        }
    }
}
