package app.grocery.list.product.list.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.elements.dialog.list.ConfirmPastedListDialogProps
import app.grocery.list.commons.compose.elements.question.AppQuestionMapper
import app.grocery.list.commons.compose.elements.question.AppQuestionProps
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.domain.preview.GetProductListPreviewUseCase
import app.grocery.list.domain.product.DeleteProductUseCase
import app.grocery.list.domain.product.DisableAllProductsInCurrentListUseCase
import app.grocery.list.domain.product.EnableAllProductsInCurrentListUseCase
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.question.HowToEditProductsQuestion
import app.grocery.list.domain.question.NeedMoreListsQuestion
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
    private val productRepository: ProductRepository,
    private val deleteProduct: DeleteProductUseCase,
    private val getFormattedTemplateProducts: GetTemplateProductsUseCase,
    private val enableAllInCurrentList: EnableAllProductsInCurrentListUseCase,
    private val disableAllInCurrentList: DisableAllProductsInCurrentListUseCase,
    private val productListPreviewMapper: ProductListPreviewMapper,
    private val questionMapper: AppQuestionMapper,
) : ViewModel(),
    ProductListPreviewCallbacks {

    val props = createPropsStateFlow()
    private val dialog = MutableStateFlow<ProductListPreviewDialogProps?>(null)
    private val events = Channel<Event>(Channel.UNLIMITED)

    private fun createPropsStateFlow() =
        getPreview
            .execute()
            .map(productListPreviewMapper::transform)
            .customStateIn(this)

    override fun onDelete(productId: Int) {
        viewModelScope.launch {
            val deletedProduct = deleteProduct.execute(productId = productId)
            val event = Event.OnProductDeleted(deletedProduct)
            events.trySend(event)
        }
    }

    override fun onProductEnabledChange(productId: Int, newValue: Boolean) {
        viewModelScope.launch {
            productRepository.setEnabled(
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
            enableAllInCurrentList.execute()
        }
    }

    override fun onDisableEnableAll() {
        viewModelScope.launch {
            disableAllInCurrentList.execute()
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
            productRepository.put(products)
        }
    }

    override fun onQuestionClose(question: AppQuestionProps) {
        viewModelScope.launch {
            questionMapper
                .toDomain(question)
                .close()
        }
    }

    override fun onQuestionClick(question: AppQuestionProps) {
        val mapped = questionMapper.toDomain(question)
        when (mapped) {
            is HowToEditProductsQuestion -> {
                dialog.value = ProductListPreviewDialogProps.HowToEditProducts
            }
            is NeedMoreListsQuestion -> {
                events.trySend(Event.OnNeedMoreListsClick)
            }
            else -> {
                throw UnsupportedOperationException("Unknown question: $question")
            }
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
        data object OnNeedMoreListsClick : Event()
    }
}
