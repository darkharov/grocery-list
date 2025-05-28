package app.grocery.list.product.input.form.screen

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.Product
import app.grocery.list.product.input.form.screen.elements.category.picker.CategoryProps
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductInputFormViewModel @Inject constructor(
    private val repository: AppRepository,
    private val categoryMapper: ProductCategoryMapper,
) : ViewModel(),
    ProductInputFormCallbacks {

    private val productTitle = MutableStateFlow(TextFieldValue())
    private val explicitlySelectedCategory = MutableStateFlow<CategoryProps?>(null)

    private val props: StateFlow<ProductInputFormProps?> = createPropsFlow()
    private val events = Channel<Event>()

    private fun createPropsFlow(): StateFlow<ProductInputFormProps?> =
        combine(
            productTitle,
            categories(),
            selectedCategory(),
            repository.atLeastOneProductAdded(),
        ) {
                productTitle,
                categories,
                selectedCategory,
                atLeastOneProductAdded,
            ->
            ProductInputFormProps(
                title = productTitle,
                categories = categories,
                selectedCategory = selectedCategory,
                atLeastOneProductAdded = atLeastOneProductAdded,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    private fun categories(): Flow<ImmutableList<CategoryProps>> =
        flow {
            val categories = repository.categories()
            val mapped = categoryMapper.transformList(categories)
            emit(mapped)
        }

    private fun selectedCategory(): Flow<CategoryProps?> =
        combine(
            explicitlySelectedCategory,
            productTitle,
        ) { explicitlySelected, productTitle ->
            explicitlySelected ?: tryToDefineCategory(productTitle = productTitle.text)
        }

    private suspend fun tryToDefineCategory(productTitle: String): CategoryProps? {
        val defined = repository.findCategory(search = productTitle)
        return categoryMapper.transformNullable(defined)
    }

    override fun onProductTitleChange(newValue: TextFieldValue) {
        productTitle.value = newValue
    }

    override fun onProductInputComplete(productTitle: String, categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val product =
                Product(
                    id = 0,
                    title = productTitle,
                    categoryId = categoryId,
                )
            repository.putProduct(product)
        }
        clearInputFields()
    }

    private fun clearInputFields() {
        productTitle.value = TextFieldValue("")
        explicitlySelectedCategory.value = null
    }

    override fun onCategorySelected(category: CategoryProps) {
        explicitlySelectedCategory.value = category
    }

    override fun onReadyToGoToPreview() {
        events.trySend(Event.OnGoToPreview)
    }

    fun props(): StateFlow<ProductInputFormProps?> =
        props

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnGoToPreview,
    }
}
