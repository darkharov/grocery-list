package app.grocery.list.final_.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@HiltViewModel
class FinalStepsViewModel @Inject constructor(
    private val repository: AppRepository,
) : ViewModel() {

    private val events = Channel<Event>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        viewModelScope.launch {
            repository
                .numberOfEnabledProducts()
                .filter { it == 0 }
                .flowOn(Dispatchers.IO)
                .collect {
                    events.trySend(Event.OnNoEnabledProductsAnymore)
                }
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnNoEnabledProductsAnymore : Event()
    }
}
