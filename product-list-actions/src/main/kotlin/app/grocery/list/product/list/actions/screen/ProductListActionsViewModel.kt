package app.grocery.list.product.list.actions.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListActionsViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val events = Channel<Event>()

    override fun onClearList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProducts()
            events.trySend(Event.OnListCleared)
        }
    }

    override fun onExitFromApp() {
        events.trySend(Event.OnExitFromApp)
    }

    override fun onStartShopping() {
        events.trySend(Event.OnStartShopping)
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnListCleared,
        OnExitFromApp,
        OnStartShopping,
    }
}
