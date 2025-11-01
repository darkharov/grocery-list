package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.format.FormatTemplateProductsUseCase
import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.list.preview.GetProductListPreviewUseCase
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPreviewViewModel @Inject constructor(
    getPreview: GetProductListPreviewUseCase,
    private val repository: ProductRepository,
    private val getFormattedTemplateProducts: FormatTemplateProductsUseCase,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props: StateFlow<ProductListPreviewProps?> =
        getPreview
            .execute()
            .map(ProductListPreviewMapper::transform)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val dialog = MutableStateFlow<ProductListPreviewDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)

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

    override fun onTemplateClick(template: ProductListPreviewProps.Empty.Template) {
        viewModelScope.launch(Dispatchers.IO) {
            val products = getFormattedTemplateProducts.execute(
                templateId = template.id,
                separator = ProductListSeparator.Dialog,
            )
            dialog.value = ProductListPreviewDialogProps.ConfirmPastedProductsWrapper(
                ConfirmPastedListDialogProps(
                    title = StringValue.StringWrapper(template.title),
                    text = products.formattedString,
                    productList = products.originalProducts,
                )
            )
        }
    }

    override fun onDialogDismiss() {
        removeDialog()
    }

    override fun onPasteProductsConfirmed(products: List<Product>) {
        removeDialog()
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProducts(products)
        }
    }

    private fun removeDialog() {
        dialog.value = null
    }

    fun events(): ReceiveChannel<Event> =
        events

    fun dialog(): StateFlow<ProductListPreviewDialogProps?> =
        dialog

    sealed class Event {
        data class OnProductDeleted(val product: Product) : Event()
        data class OnEditProduct(val productId: Int) : Event()
    }
}
