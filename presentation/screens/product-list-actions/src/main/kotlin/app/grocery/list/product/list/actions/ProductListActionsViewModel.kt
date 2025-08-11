package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.format.ProductListToStringFormatter
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.EnabledAndDisabledProducts
import app.grocery.list.domain.Product
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
    private val productListFormatter: ProductListToStringFormatter,
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

            val products = repository
                .enabledAndDisabledProducts()
                .flowOn(Dispatchers.IO)
                .first()

            loadingListToShare.value = false

            val all = products.all

            if (products.mixed) {
                dialog.value = ProductListActionsDialog.SublistToSharePicker(
                    productListSize = all.size,
                    enabledItemsCount = products.enabled.size,
                    disabledItemsCount = products.disabled.size,
                    payload = products,
                )
            } else {
                sendShareEvent(products = all)
            }
        }
    }

    private fun sendShareEvent(products: List<Product>) {
        events.trySend(Event.OnShare(products))
    }

    override fun onShareAll(dialog: ProductListActionsDialog.SublistToSharePicker) {
        sendShareEvent((dialog.payload as EnabledAndDisabledProducts).all)
    }

    override fun onShareEnabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker) {
        sendShareEvent((dialog.payload as EnabledAndDisabledProducts).enabled)
    }

    override fun onShareDisabledOnly(dialog: ProductListActionsDialog.SublistToSharePicker) {
        sendShareEvent((dialog.payload as EnabledAndDisabledProducts).disabled)
    }

    override fun onPaste(text: String) {
        productListFormatter
            .parse(message = text)
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
            dialog.value = ProductListActionsDialog.ProductSuccessfullyAdded(
                count = productList.size,
            )
        }
    }

    override fun onAddProducts(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProducts(productList)
            dialog.value = ProductListActionsDialog.ProductSuccessfullyAdded(
                count = productList.size,
            )
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
