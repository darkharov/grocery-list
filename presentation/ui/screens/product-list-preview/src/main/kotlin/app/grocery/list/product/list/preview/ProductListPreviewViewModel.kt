package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.preview.GetProductListPreviewUseCase
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.template.GetTemplateProductsUseCase
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPreviewViewModel @Inject constructor(
    private val getPreview: GetProductListPreviewUseCase,
    private val repository: ProductRepository,
    private val getFormattedTemplateProducts: GetTemplateProductsUseCase,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props = createPropsStateFlow()
    private val dialog = MutableStateFlow<ProductListPreviewDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)

    private fun createPropsStateFlow() =
        getPreview
            .execute()
            .map(ProductListPreviewMapper::transform)
            .customStateIn(this)

    override fun onDelete(productId: Int) {
        viewModelScope.launch {
            val deletedProduct = repository.delete(productId = productId)
            val event = Event.OnProductDeleted(deletedProduct)
            events.trySend(event)
        }
    }

    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {
        viewModelScope.launch {
            repository.setEnabled(
                productId = productId,
                enabled = newValue,
            )
        }
    }

    override fun onEditProduct(productId: Int) {
        events.trySend(Event.OnEditProduct(productId = productId))
    }

    override fun onEnableAll() {
        viewModelScope.launch {
            repository.enableAll()
        }
    }

    override fun onDisableEnableAll() {
        viewModelScope.launch {
            repository.disableAll()
        }
    }

    override fun onTemplateClick(template: ProductListPreviewProps.Empty.Template) {
        viewModelScope.launch {
            val products = getFormattedTemplateProducts.execute(templateId = template.id)
            dialog.value = ProductListPreviewDialogProps.ConfirmPastedProductsWrapper(
                ConfirmPastedListDialogProps(
                    title = StringValue.StringWrapper(template.title),
                    text = products.formattedTitles,
                    productList = products.products,
                )
            )
        }
    }

    override fun onDialogDismiss() {
        removeDialog()
    }

    override fun onPasteProductsConfirmed(products: List<Product>) {
        removeDialog()
        viewModelScope.launch {
            repository.put(products)
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
