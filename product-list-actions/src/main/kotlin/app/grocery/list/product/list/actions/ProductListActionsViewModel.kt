package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.notifications.NotificationPublisher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListActionsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val notificationPublisher: NotificationPublisher,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val events = Channel<Event>()

    override fun onClearListOptionSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProducts()
            events.trySend(Event.OnListCleared)
        }
    }

    override fun onExitOptionSelected() {
        events.trySend(Event.OnExitOptionSelected)
    }

    override fun onStartShoppingSelected() {
        events.trySend(Event.OnStartShoppingOptionSelected)
    }

    fun postNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            val productList = repository.getProductList().first()
            notificationPublisher.tryToPost(productList)
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnListCleared,
        OnExitOptionSelected,
        OnStartShoppingOptionSelected,
    }
}
