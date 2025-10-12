package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.format.GetProductTitleFormatter
import app.grocery.list.commons.format.ProductListParser
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.EnabledAndDisabledProducts
import app.grocery.list.domain.Product
import app.grocery.list.product.list.actions.dialog.ProductListActionsDialogProps
import commons.android.stateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListActionsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val productListParser: ProductListParser,
    private val getProductTitleFormatter: GetProductTitleFormatter,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val dialog = MutableStateFlow<ProductListActionsDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)
    private val loadingListToShare = MutableStateFlow(false)

    val props =
        combine(
            repository.bottomBarRoadmapStep.observe(),
            repository.numberOfProducts(),
            repository.atLeastOneProductEnabled(),
            loadingListToShare,
        ) {
                bottomBarRoadMapStep,
                numberOfProducts,
                atLeastOneProductEnabled,
                loadingListToShare,
            ->
            ProductListActionsProps(
                useIconsOnBottomBar = bottomBarRoadMapStep.useIcons,
                numberOfProducts = numberOfProducts,
                loadingListToShare = loadingListToShare,
                atLeastOneProductEnabled = atLeastOneProductEnabled,
            )
        }.stateIn(this)

    override fun onClearListConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            dialog.value = null
            repository.clearProducts()
        }
    }

    override fun onDialogDismiss() {
        dialog.value = null
    }

    override fun onNoEnabledProductsToStartShopping(productCount: Int) {
        dialog.value =
            ProductListActionsDialogProps.EnableAllAndStartShopping(
                totalProductCount = productCount,
            )
    }

    override fun onEnableAllAndStartShopping() {
        dialog.value = null
        viewModelScope.launch(Dispatchers.IO) {
            repository.enableAllProducts()
            events.trySend(Event.OnStartShopping)
        }
    }

    private fun sendShareEventAndRemoveDialog(products: List<Product>) {
        events.trySend(Event.OnShare(products))
        dialog.value = null
    }

    override fun onShareAll(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        sendShareEventAndRemoveDialog((dialog.payload as EnabledAndDisabledProducts).all)
    }

    override fun onShareEnabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        sendShareEventAndRemoveDialog((dialog.payload as EnabledAndDisabledProducts).enabled)
    }

    override fun onShareDisabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        sendShareEventAndRemoveDialog((dialog.payload as EnabledAndDisabledProducts).disabled)
    }

    override fun onPasted(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = productListParser.parse(string = text)) {
                is ProductListParser.Result.EncodedStringParsed -> {
                    handleParsedProducts(result.products)
                }
                is ProductListParser.Result.HandTypedStringParsed -> {
                    val products = result.products
                    viewModelScope.launch(Dispatchers.IO) {
                        val formatter = getProductTitleFormatter
                            .execute()
                            .first()
                        dialog.value = ProductListActionsDialogProps.ConfirmHandTypedList(
                            numberOfFoundProducts = products.size,
                            itemTitles = formatter.printToString(products, separator = "\n"),
                            productList = products,
                        )
                    }
                }
                is ProductListParser.Result.ProductsNotFound -> {
                    dialog.value = ProductListActionsDialogProps.CopiedProductListNotFound
                }
            }
        }
    }

    override fun onPasteHandTypedProducts(productList: List<Product>) {
        handleParsedProducts(productList)
    }

    private fun handleParsedProducts(products: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfAddedProducts = repository.numberOfProducts().first()
            if (numberOfAddedProducts == 0) {
                addProducts(products)
            } else {
                dialog.value = ProductListActionsDialogProps.HowToPutPastedProducts(
                    productList = products,
                )
            }
        }
    }

    private fun addProducts(products: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProducts(products)
            dialog.value = ProductListActionsDialogProps.ProductSuccessfullyAdded(
                count = products.size,
            )
        }
    }

    override fun onReplaceProductsBy(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProducts()
            repository.putProducts(productList)
            dialog.value = ProductListActionsDialogProps.ProductSuccessfullyAdded(
                count = productList.size,
            )
        }
    }

    override fun onAddProducts(productList: List<Product>) {
        addProducts(productList)
    }

    override fun onAttemptToClearList() {
        dialog.value = ProductListActionsDialogProps.ConfirmClearList
    }

    override fun onAdd() {
        events.trySend(Event.OnGoToProductInputForm)
    }

    override fun onGoToActions() {
        events.trySend(Event.OnGoToActions)
    }

    override fun onExitFromApp() {
        events.trySend(Event.OnExitFromApp)
    }

    override fun onAttemptToStartShopping(atLeastOneProductEnabled: Boolean, numberOfProducts: Int) {
        if (atLeastOneProductEnabled) {
            events.trySend(Event.OnStartShopping)
        } else {
            dialog.value = ProductListActionsDialogProps.EnableAllAndStartShopping(
                totalProductCount = numberOfProducts,
            )
        }
    }

    override fun onAttemptToShareCurrentList() {
        handleShareCurrentListAction()
    }

    override fun onAttemptToPaste() {
        events.trySend(Event.OnPasteCopiedList)
    }

    private fun handleShareCurrentListAction() {
        loadingListToShare.value = true

        viewModelScope.launch(Dispatchers.IO) {

            val products = repository
                .enabledAndDisabledProducts()
                .first()

            loadingListToShare.value = false

            val all = products.all

            if (products.mixed) {
                dialog.value = ProductListActionsDialogProps.SublistToSharePicker(
                    productListSize = all.size,
                    enabledItemsCount = products.enabled.size,
                    disabledItemsCount = products.disabled.size,
                    payload = products,
                )
            } else {
                events.trySend(Event.OnShare(all))
            }
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    fun dialog(): StateFlow<ProductListActionsDialogProps?> =
        dialog.asStateFlow()

    sealed class Event {

        data object OnExitFromApp : Event()
        data object OnPasteCopiedList : Event()
        data object OnStartShopping : Event()
        data object OnGoToProductInputForm : Event()
        data object OnGoToActions : Event()
        data object OnGoBack : Event()

        data class OnShare(val products: List<Product>) : Event()
    }
}
