package app.grocery.list.product.list.actions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.R
import app.grocery.list.commons.compose.elements.button.AppButtonStateProps
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.product.AtLeastOneProductInCurrentListUseCase
import app.grocery.list.domain.product.ClearCurrentListUseCase
import app.grocery.list.domain.product.EnableAllProductsInCurrentListUseCase
import app.grocery.list.domain.product.EnabledAndDisabledProducts
import app.grocery.list.domain.product.GetNumberOfProductsInSelectedListUseCase
import app.grocery.list.domain.product.GroupEnabledAndDisabledProductsUseCase
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.product.RewriteCurrentListUseCase
import app.grocery.list.domain.settings.SettingsRepository
import app.grocery.list.domain.sharing.GetProductSharingStringUseCase
import app.grocery.list.domain.sharing.ParseAndFormatProductsUseCase
import app.grocery.list.product.list.actions.dialog.ProductListActionsDialogProps
import app.grocery.list.storage.value.kotlin.get
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
    private val groupEnabledAndDisabled: GroupEnabledAndDisabledProductsUseCase,
    private val settingsRepository: SettingsRepository,
    private val getProductSharingString: GetProductSharingStringUseCase,
    private val parseProductList: ParseAndFormatProductsUseCase,
    private val atLeastOneProductInCurrentList: AtLeastOneProductInCurrentListUseCase,
    private val getNumberOfProducts: GetNumberOfProductsInSelectedListUseCase,
    private val clearCurrentList: ClearCurrentListUseCase,
    private val enableAllProductsInCurrentList: EnableAllProductsInCurrentListUseCase,
    private val rewriteCurrentList: RewriteCurrentListUseCase,
) : ViewModel(),
    ProductListActionsCallbacks {

    private val dialog = MutableStateFlow<ProductListActionsDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)

    val props =
        combine(
            settingsRepository.bottomBarRoadmapStep.observe(),
            atLeastOneProductInCurrentList.execute(enabledOnly = false),
        ) {
                bottomBarRoadMapStep,
                atLeastOneProduct,
            ->
            ProductListActionsProps(
                useIconsOnBottomBar = bottomBarRoadMapStep.useIcons,
                exitButtonTitle = if (atLeastOneProduct) {
                    StringValue.ResId(R.string.save_and_exit)
                } else {
                    StringValue.ResId(R.string.exit)
                },
                listActionButtonsState = AppButtonStateProps
                    .enabled(atLeastOneProduct),
            )
        }.customStateIn(this)

    override fun onClearListConfirmed() {
        viewModelScope.launch {
            dialog.value = null
            clearCurrentList.execute()
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
        viewModelScope.launch {
            enableAllProductsInCurrentList.execute()
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
        viewModelScope.launch {
            dialog.value = ProductListActionsDialogProps.ConfirmSharing(
                numberOfProducts = products.size,
                recommendUsingThisApp = settingsRepository.recommendAppWhenSharingList.get(),
                payload = products,
            )
        }
    }

    override fun onSharingConfirmed(dialog: ProductListActionsDialogProps.ConfirmSharing) {
        this.dialog.value = null
        val sharingString = getProductSharingString.execute(
            products = dialog.payload,
            recommendUsingThisApp = dialog.recommendUsingThisApp,
        )
        val event = Event.OnShare(sharingString = sharingString)
        events.trySend(event)
    }

    override fun onRecommendThisAppCheckedClick(dialog: ProductListActionsDialogProps.ConfirmSharing) {
        val recommendUsingThisApp = !(dialog.recommendUsingThisApp)
        this.dialog.value = dialog.copy(recommendUsingThisApp = recommendUsingThisApp)
        viewModelScope.launch {
            settingsRepository.recommendAppWhenSharingList.set(recommendUsingThisApp)
        }
    }

    override fun onAttemptToPaste() {
        events.trySend(Event.OnPasteCopiedList)
    }

    override fun onPasted(text: String) {
        viewModelScope.launch {
            parseProductList
                .execute(text = text)
                .onSuccess { products ->
                    dialog.value = ProductListActionsDialogProps.ConfirmPastedListWrapper(
                        ConfirmPastedListDialogProps(
                            title = StringValue.PluralResId(
                                resId = R.plurals.pattern_products_found,
                                count = products.products.size,
                                useCountAsArgument = true,
                            ),
                            text = products.formattedTitles,
                            productList = products.products,
                        )
                    )
                }
                .onFailure {
                    dialog.value = ProductListActionsDialogProps.CopiedProductListNotFound
                }
        }
    }

    override fun onPasteProductsConfirmed(products: List<Product>) {
        viewModelScope.launch {
            val atLeastOneProductInCurrentList =
                atLeastOneProductInCurrentList
                    .execute(enabledOnly = false)
                    .first()
            if (atLeastOneProductInCurrentList) {
                dialog.value = ProductListActionsDialogProps.HowToPutPastedProducts(
                    productList = products,
                )
            } else {
                addProducts(products)
            }
        }
    }

    private fun addProducts(products: List<Product>) {
        viewModelScope.launch {
            productRepository.put(products)
            dialog.value = ProductListActionsDialogProps.ProductSuccessfullyAdded(
                count = products.size,
            )
        }
    }

    override fun onReplaceProductsBy(productList: List<Product>) {
        viewModelScope.launch {
            rewriteCurrentList.execute(productList)
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

    override fun onAttemptToStartShopping() {
        viewModelScope.launch {
            val atLeastOneProductEnabled = atLeastOneProductInCurrentList.execute(enabledOnly = true).first()
            if (atLeastOneProductEnabled) {
                events.trySend(Event.OnStartShopping)
            } else {
                dialog.value = ProductListActionsDialogProps.EnableAllAndStartShopping(
                    totalProductCount = getNumberOfProducts.execute(enabledOnly = false).first(),
                )
            }
        }
    }

    override fun onAttemptToShareCurrentList() {
        handleShareCurrentListAction()
    }

    private fun handleShareCurrentListAction() {

        viewModelScope.launch {

            val products = groupEnabledAndDisabled.execute()

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
