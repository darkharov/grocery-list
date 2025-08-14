package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.format.GetProductTitleFormatter
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPreviewViewModel @Inject constructor(
    private val mapperFactory: ProductListMapper.Factory,
    private val getProductTitleFormatter: GetProductTitleFormatter,
    private val repository: AppRepository,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props: StateFlow<ProductListPreviewProps?> = collectProps()

    private val events = Channel<Event>(Channel.UNLIMITED)

    private fun collectProps() = combine(
        getProductTitleFormatter.execute().map(mapperFactory::create),
        repository.categorizedProducts(),
        ProductListMapper::transform,
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null,
    )

    override fun onDelete(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val deletedProduct = repository.deleteProduct(productId = productId)
            val event = Event.OnProductDeleted(deletedProduct)
            events.trySend(event)
        }
    }

    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setProductEnabled(
                productId = productId,
                enabled = newValue,
            )
        }
    }

    override fun onAddClick() {
        events.trySend(Event.OnAdd)
    }

    override fun onNextClick() {
        events.trySend(Event.OnNext)
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data object OnAdd : Event()
        data object OnNext : Event()
        data class OnProductDeleted(val product: Product) : Event()
    }
}
