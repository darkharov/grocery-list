package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.Product
import app.grocery.list.sharing.ParseCopiedProductList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListActionsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val parsePastedProductList: ParseCopiedProductList,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val loadingListToShare = MutableStateFlow(false)

    val props = combine(
        repository.numberOfAddedProducts(),
        loadingListToShare,
    ) { numberOfAddedProducts, loadingListToShare ->
        ProductListActionsProps(
            loadingListToShare = loadingListToShare,
            numberOfProducts = numberOfAddedProducts,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    private val dialog = MutableStateFlow<ProductListActionsDialog?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)

    override fun onGoToClearListConfirmation() {
        dialog.value = ProductListActionsDialog.ConfirmClearList
    }

    override fun onClearListConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            dialog.value = null
            repository.clearProducts()
        }
    }

    override fun onDialogDismiss() {
        dialog.value = null
    }

    override fun onExitFromApp() {
        events.trySend(Event.OnExitFromApp)
    }

    override fun onStartShopping() {
        events.trySend(Event.OnStartShopping)
    }

    override fun onShare() {
        loadingListToShare.value = true
        viewModelScope.launch {
            val products = repository.products().flowOn(Dispatchers.IO).first()
            loadingListToShare.value = false
            events.trySend(Event.OnShare(products))
        }
    }

    override fun onPaste(text: String) {
        parsePastedProductList.execute(message = text)
            .onFailure {
                dialog.value = ProductListActionsDialog.CopiedProductListNotFound
            }
            .onSuccess { pastedProducts ->
                viewModelScope.launch {
                    val numberOfAddedProducts = repository.numberOfAddedProducts()
                        .flowOn(Dispatchers.IO)
                        .first()
                    if (numberOfAddedProducts == 0) {
                        onAddProducts(pastedProducts)
                    } else {
                        dialog.value = ProductListActionsDialog.HowToPutPastedProducts(
                            productList = pastedProducts,
                        )
                    }
                }
            }
    }

    override fun onReplaceProductsBy(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProducts()
            repository.putProducts(productList)
            dialog.value = null
        }
    }

    override fun onAddProducts(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProducts(productList)
            dialog.value = null
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    fun dialog(): StateFlow<ProductListActionsDialog?> =
        dialog.asStateFlow()

    sealed class Event {

        data object OnExitFromApp : Event()
        data object OnStartShopping : Event()

        data class OnShare(val products: List<Product>) : Event()
    }
}
