package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPreviewViewModel @Inject constructor(
    private val mapper: ProductListMapper,
    private val repository: AppRepository,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props: StateFlow<ProductListPreviewProps?> =
        repository
            .productList()
            .map(mapper::transform)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val events = Channel<Event>(Channel.UNLIMITED)

    override fun onDelete(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(productId = productId)
        }
    }

    override fun onNext() {
        events.trySend(Event.OnGoToActions)
    }

    override fun onAddProduct() {
        events.trySend(Event.OnAddProduct)
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnAddProduct,
        OnGoToActions,
    }
}
