package app.grocery.list.custom.product.lists.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.custom.product.lists.picker.dialog.ProductListPickerDialogProps
import app.grocery.list.custom.product.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListIdMapper
import app.grocery.list.custom.product.lists.picker.item.mappers.ProductListPickerItemMapper
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.product.list.SummarizeProductListsUseCase
import app.grocery.list.kotlin.SimpleBuffer
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val dialog = MutableStateFlow<ProductListPickerDialogProps?>(null)
    private val idsOfExcludedOnes = SimpleBuffer<ProductList.Id>()

    val props =
        combine(
            summarizeProductLists.execute(),
            idsOfExcludedOnes.observe(),
            ProductListPickerMapper::Params,
        ).map(pickerMapper::toPresentation)
            .customStateIn(this)

    override fun onSelect(item: ProductListPickerItemProps) {
        viewModelScope.launch {
            val productListId = productListIdMapper.toDomain(itemId = item.id)
            productListRepository.setSelectedOne(productListId)
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

    override fun onAddClick() {
        events.trySend(Event.OnAddNew)
    }

    override fun onDelete(item: ProductListPickerItemProps) {
        idsOfExcludedOnes += productListIdMapper.toDomain(item.id)
        dialog.value =
            ProductListPickerDialogProps.CustomListDeletionConfirmation(
                id = item.id,
                title = item.title,
            )
    }

    override fun onDeletionConfirmed(customProductListId: String) {
        viewModelScope.launch {
            val id = productListIdMapper.toDomain(customProductListId)
            when (id) {
                is ProductList.Id.Custom -> {
                    productListRepository.delete(id)
                }
                is ProductList.Id.Default -> {
                    throw IllegalStateException("The default list should not be deletable")
                }
            }
            dialog.value = null
            idsOfExcludedOnes -= id
        }
    }

    override fun onDeletionRejected(customProductListId: String) {
        dialog.value = null
        idsOfExcludedOnes -= productListIdMapper.toDomain(customProductListId)
    }

    fun events(): ReceiveChannel<Event> =
        events

    fun dialog(): StateFlow<ProductListPickerDialogProps?> =
        dialog.asStateFlow()

    sealed class Event {
        data class OnEdit(val customListId: ProductList.Id.Custom) : Event()
        data object OnAddNew : Event()
    }
}
