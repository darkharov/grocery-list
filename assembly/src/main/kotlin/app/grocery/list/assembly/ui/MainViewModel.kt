package app.grocery.list.assembly.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.assembly.ui.content.AppEvent
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: AppRepository,
) : ViewModel() {

    private val appEvents = Channel<AppEvent>(capacity = Channel.UNLIMITED)

    val numberOfAddedProducts =
        repository
            .getNumberOfAddedProducts()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val productList =
        repository
            .getProductList()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun appEvents(): ReceiveChannel<AppEvent> =
        appEvents

    fun notifyPushNotificationsGranted() {
        appEvents.trySend(AppEvent.PushNotificationsGranted)
    }
}
