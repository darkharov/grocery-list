package app.grocery.list.final_.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.product.AtLeastOneProductInCurrentListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

@HiltViewModel
class FinalStepsViewModel @Inject constructor(
    private val atLeastOneProductInCurrentList: AtLeastOneProductInCurrentListUseCase,
) : ViewModel() {

    private val events = Channel<Event>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        viewModelScope.launch {
            atLeastOneProductInCurrentList
                .execute(enabledOnly = true)
                .collect { atLeastOne ->
                    if (!(atLeastOne)) {
                        events.trySend(Event.OnNoEnabledProductsAnymore)
                        return@collect
                    }
                }
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnNoEnabledProductsAnymore : Event()
    }
}
