package app.grocery.list.custom.product.lists.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListIdMapper
import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListPickerItemMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.product.list.SummarizeProductListsUseCase
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPickerViewModel @Inject constructor(
    pickerMapper: ProductListPickerMapper,
    summarizeProductLists: SummarizeProductListsUseCase,
    private val productListIdMapper: ProductListIdMapper,
    private val itemMapper: ProductListPickerItemMapper,
    private val productListRepository: ProductListRepository,
) : ViewModel(),
    ProductListPickerCallbacks {

    private val events = Channel<Event>(Channel.UNLIMITED)

    val props = summarizeProductLists
        .execute()
        .map(pickerMapper::toPresentation)
        .customStateIn(this)

    override fun onSelect(item: ProductListPickerItemProps) {
        viewModelScope.launch {
            val productListId = productListIdMapper.toDomain(itemId = item.id)
            productListRepository.setSelectedOne(productListId)
            events.trySend(Event.OnExit)
        }
    }

    override fun onEdit(item: ProductListPickerItemProps) {
        viewModelScope.launch {
            val summary = itemMapper.toDomain(item)
            when (val listId = summary.productList.id) {
                is ProductList.Id.Custom -> {
                    val event = Event.OnEdit(
                        customListId = listId,
                    )
                    events.trySend(event)
                }
                is ProductList.Id.Default -> {
                    // The default list is not editable, so nothing to do...
                }
            }
        }
    }

    override fun onDelete(item: ProductListPickerItemProps) {
        viewModelScope.launch {
            when (val id = productListIdMapper.toDomain(item.id)) {
                is ProductList.Id.Custom -> {
                    productListRepository.delete(id)
                }
                is ProductList.Id.Default -> {
                    throw IllegalStateException("The default list should not be deletable")
                }
            }
        }
    }

    override fun onAddClick() {
        events.trySend(Event.OnAddNew)
    }

    fun events(): ReceiveChannel<Event> =
        events

    sealed class Event {
        data class OnEdit(val customListId: ProductList.Id.Custom) : Event()
        data object OnAddNew : Event()
        data object OnExit : Event()
    }
}
