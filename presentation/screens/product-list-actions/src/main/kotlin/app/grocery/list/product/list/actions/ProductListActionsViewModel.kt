package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.format.sharing.ParseProductListUseCase
import app.grocery.list.domain.format.sharing.ShareProductListUseCase
import app.grocery.list.domain.product.EnabledAndDisabledProducts
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.product.list.actions.dialog.ProductListActionsDialogProps
import app.grocery.list.storage.value.kotlin.get
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
    private val productRepository: ProductRepository,
    private val appRepository: AppRepository,
    private val shareProductList: ShareProductListUseCase,
    private val parseProductList: ParseProductListUseCase,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val dialog = MutableStateFlow<ProductListActionsDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)
    private val loadingListToShare = MutableStateFlow(false)

    val props =
        combine(
            appRepository.bottomBarRoadmapStep.observe(),
            productRepository.numberOfProducts(),
            productRepository.atLeastOneProductEnabled(),
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
            productRepository.clearProducts()
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
            productRepository.enableAllProducts()
            events.trySend(Event.OnStartShopping)
        }
    }

    override fun onShareAll(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        showConfirmSharingDialog((dialog.payload as EnabledAndDisabledProducts).all)
    }

    override fun onShareEnabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        showConfirmSharingDialog((dialog.payload as EnabledAndDisabledProducts).enabled)
    }

    override fun onShareDisabledOnly(dialog: ProductListActionsDialogProps.SublistToSharePicker) {
        showConfirmSharingDialog((dialog.payload as EnabledAndDisabledProducts).disabled)
    }

    private fun showConfirmSharingDialog(products: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            dialog.value = ProductListActionsDialogProps.ConfirmSharing(
                numberOfProducts = products.size,
                recommendUsingThisApp = appRepository.recommendAppWhenSharingList.get(),
                payload = products,
            )
        }
    }

    override fun onSharingConfirmed(dialog: ProductListActionsDialogProps.ConfirmSharing) {
        this.dialog.value = null
        val sharingString = shareProductList.execute(
            products = dialog.payload,
            recommendUsingThisApp = dialog.recommendUsingThisApp,
        )
        val event = Event.OnShare(sharingString = sharingString)
        events.trySend(event)
    }

    override fun onRecommendThisAppCheckedClick(dialog: ProductListActionsDialogProps.ConfirmSharing) {
        val recommendUsingThisApp = !(dialog.recommendUsingThisApp)
        this.dialog.value = dialog.copy(recommendUsingThisApp = recommendUsingThisApp)
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.recommendAppWhenSharingList.set(recommendUsingThisApp)
        }
    }

    override fun onAttemptToPaste() {
        events.trySend(Event.OnPasteCopiedList)
    }

    override fun onPasted(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            parseProductList
                .execute(
                    text = text,
                    separator = ProductListSeparator.Dialog,
                )
                .onSuccess { products ->
                    dialog.value = ProductListActionsDialogProps.ConfirmPastedListWrapper(
                        ConfirmPastedListDialogProps(
                            title = StringValue.PluralResId(
                                resId = R.plurals.pattern_products_found,
                                count = products.originalProducts.size,
                                useCountAsArgument = true,
                            ),
                            text = products.formattedString,
                            productList = products.originalProducts,
                        )
                    )
                }
                .onFailure {
                    dialog.value = ProductListActionsDialogProps.CopiedProductListNotFound
                }
        }
    }

    override fun onPasteProductsConfirmed(products: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            val numberOfAddedProducts = productRepository.numberOfProducts().first()
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
            productRepository.putProducts(products)
            dialog.value = ProductListActionsDialogProps.ProductSuccessfullyAdded(
                count = products.size,
            )
        }
    }

    override fun onReplaceProductsBy(productList: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.clearProducts()
            productRepository.putProducts(productList)
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

    private fun handleShareCurrentListAction() {
        loadingListToShare.value = true

        viewModelScope.launch(Dispatchers.IO) {

            val products = productRepository
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
                showConfirmSharingDialog(products = products.all)
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

        data class OnShare(val sharingString: String) : Event()
    }
}
