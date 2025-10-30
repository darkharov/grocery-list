package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: AppRepository,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props: StateFlow<ProductListPreviewProps?> = collectProps()

    private val events = Channel<Event>(Channel.UNLIMITED)

    private fun collectProps() = combine(
        repository.productTitleFormatter.observe().map(mapperFactory::create),
        repository.categorizedProducts(AppRepository.CategorizedProductsCriteria.All),
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

    override fun onEditProduct(productId: Int) {
        events.trySend(Event.OnEditProduct(productId = productId))
    }

    override fun onEnableAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.enableAllProducts()
        }
    }

    override fun onDisableEnableAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disableAllProducts()
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data class OnProductDeleted(val product: Product) : Event()
        data class OnEditProduct(val productId: Int) : Event()
    }
}
