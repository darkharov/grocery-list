package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.format.GetProductTitleFormatter
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
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

    val props: StateFlow<ProductListPreviewProps?> =
        combine(
            mapper(),
            repository.categorizedProducts(
                criteria = AppRepository.CategorizedProductsCriteria.All,
            ),
            ProductListMapper::transform,
        ).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    private fun mapper(): Flow<ProductListMapper> =
        getProductTitleFormatter
            .execute()
            .map(mapperFactory::create)

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

    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setProductEnabled(
                productId = productId,
                enabled = newValue,
            )
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnAddProduct,
        OnGoToActions,
    }
}
