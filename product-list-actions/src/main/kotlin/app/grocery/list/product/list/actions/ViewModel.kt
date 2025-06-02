package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListActionsViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val events = Channel<Event>()
    private val dialog = MutableStateFlow<ProductListActionsDialog?>(null)

    override fun onGoToClearListConfirmation() {
        dialog.value = ProductListActionsDialog.ConfirmClearList
    }

    override fun onClearListConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            dialog.value = null
            repository.clearProducts()
            events.trySend(Event.OnListCleared)
        }
    }

    override fun onClearListDenied() {
        dialog.value = null
    }

    override fun onExitFromApp() {
        events.trySend(Event.OnExitFromApp)
    }

    override fun onStartShopping() {
        events.trySend(Event.OnStartShopping)
    }

    fun events(): ReceiveChannel<Event> =
        events

    fun dialog(): StateFlow<ProductListActionsDialog?> =
        dialog.asStateFlow()

    enum class Event {
        OnListCleared,
        OnExitFromApp,
        OnStartShopping,
    }
}
