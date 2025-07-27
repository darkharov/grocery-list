package app.grocery.list.product.input.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.AtLeastOneProductJustAddedUseCase
import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import app.grocery.list.product.input.form.elements.category.picker.CategoryMapper
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductInputFormViewModel @Inject constructor(
    private val repository: AppRepository,
    private val atLeastOneProductJustAdded: AtLeastOneProductJustAddedUseCase,
    private val categoryMapper: CategoryMapper,
) : ViewModel(),
    ProductInputFormCallbacks {

    private val productTitle = MutableStateFlow("")
    private val explicitlySelectedCategory = MutableStateFlow<CategoryProps?>(null)
    private val categoryExpanded = MutableStateFlow(false)

    private val props = createPropsFlow()
    private val events = Channel<Event>(Channel.UNLIMITED)

    private fun createPropsFlow(): StateFlow<ProductInputFormProps?> =
        combine(
            emoji(),
            categories(),
            selectedCategory(),
            categoryExpanded,
            atLeastOneProductJustAdded.execute(),
        ) {
                emojiSearchResult,
                categories,
                selectedCategory,
                categoryExpanded,
                atLeastOneProductAdded,
            ->
            ProductInputFormProps(
                emoji = emojiSearchResult?.emoji,
                categoryPicker = CategoryPickerProps(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    expanded = categoryExpanded,
                ),
                atLeastOneProductAdded = atLeastOneProductAdded,
                payload = emojiSearchResult,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    private fun emoji(): Flow<EmojiSearchResult?> =
        productTitle.map {
            repository.findEmoji(search = it)
        }

    private fun categories(): Flow<ImmutableList<CategoryProps>> =
        repository
            .categories()
            .map(categoryMapper::transformList)

    private fun selectedCategory(): Flow<CategoryProps?> =
        combine(
            explicitlySelectedCategory,
            productTitle,
        ) { explicitlySelected, productTitle ->
            explicitlySelected ?: findCategory(productTitle = productTitle)
        }

    private suspend fun findCategory(productTitle: String): CategoryProps? {
        val defined = repository.findCategory(search = productTitle)
        return categoryMapper.transformNullable(defined)
    }

    override fun onProductTitleChange(newValue: String) {
        productTitle.value = newValue
    }

    override fun onProductInputComplete(productTitle: String, categoryId: Int, payload: Any?) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = Product(
                id = 0,
                title = productTitle.replaceFirstChar { it.uppercaseChar() },
                emojiSearchResult = payload as EmojiSearchResult?,
                categoryId = categoryId,
            )
            repository.putProduct(product)
        }
        explicitlySelectedCategory.value = null
    }

    override fun onCategoryPickerExpandChange(expanded: Boolean) {
        categoryExpanded.value = expanded
    }

    override fun onCategorySelected(category: CategoryProps) {
        explicitlySelectedCategory.value = category
        categoryExpanded.value = false
    }

    override fun onComplete() {
        events.trySend(Event.OnDone)
    }

    fun props(): StateFlow<ProductInputFormProps?> =
        props

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        OnDone,
    }
}
