package app.grocery.list.product.input.form

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.AtLeastOneProductJustAddedUseCase
import app.grocery.list.domain.EmojiSearchResult
import app.grocery.list.domain.Product
import app.grocery.list.product.input.form.elements.category.picker.CategoryMapper
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps
import commons.android.stateIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = ProductInputFormViewModel.Factory::class,
)
internal class ProductInputFormViewModel @AssistedInject constructor(
    @Assisted
    private val productId: Int?,
    private val repository: AppRepository,
    private val categoryMapper: CategoryMapper,
    atLeastOneProductJustAdded: AtLeastOneProductJustAddedUseCase,
) : ViewModel(),
    ProductInputFormCallbacks {

    private val events = Channel<Event>(Channel.UNLIMITED)

    private val title = MutableStateFlow(TextFieldValue(""))
    private val explicitlySelectedCategory = MutableStateFlow<CategoryProps?>(null)
    private val categoryPickerExpanded = MutableStateFlow(false)

    val emoji = emoji().stateIn(this)
    val categoryPicker = categoryPicker().stateIn(this, CategoryPickerProps())
    val atLeastOneProductJustAdded = atLeastOneProductJustAdded.execute().stateIn(this, defaultValue = false)

    init {
        if (productId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val titleString = repository.productTitle(productId = productId)
                title.value = TextFieldValue(
                    text = titleString,
                    selection = TextRange(
                        titleString.length,
                    ),
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun emoji(): Flow<EmojiProps?> =
        title
            .mapLatest { title ->
                repository.findEmoji(search = title.text)
            }
            .map { result ->
                if (result == null) {
                    null
                } else {
                    EmojiProps(
                        value = result.emoji,
                        payload = result,
                    )
                }
            }

    private fun categoryPicker(): Flow<CategoryPickerProps> =
        combine(
            categories(),
            selectedCategory(),
            categoryPickerExpanded,
        ) {
                categories,
                selectedCategory,
                expanded,
            ->
            CategoryPickerProps(
                categories = categories,
                selectedCategory = selectedCategory,
                expanded = expanded,
            )
        }

    private fun categories(): Flow<ImmutableList<CategoryProps>> =
        repository
            .categories()
            .map(categoryMapper::transformList)

    private fun selectedCategory(): Flow<CategoryProps?> =
        combine(
            explicitlySelectedCategory,
            title,
        ) { explicitlySelected, productTitle ->
            explicitlySelected ?: findCategory(productTitle = productTitle.text)
        }

    private suspend fun findCategory(productTitle: String): CategoryProps? {
        val defined = repository.findCategory(search = productTitle)
        return categoryMapper.transformNullable(defined)
    }

    override fun onProductTitleChange(newValue: TextFieldValue) {
        title.value = newValue
    }

    override fun onAttemptToCompleteProductInput(
        productTitle: String,
        categoryId: Int?,
        emoji: EmojiProps?,
        atLeastOneProductJustAdded: Boolean,
    ) {
        if (productTitle.isNotBlank()) {
            if (categoryId != null) {
                putProduct(
                    productTitle = productTitle,
                    categoryId = categoryId,
                    emojiSearchResult = emoji?.payload as EmojiSearchResult?,
                )
                finalizeInput()
            } else {
                handleCategoryNotSpecified()
            }
        } else {
            if (categoryId != null) {
                events.trySend(Event.TitleNotSpecified)
            } else if (atLeastOneProductJustAdded) {
                events.trySend(Event.Completed)
            } // else form is completely empty, so there's nothing to do
        }
    }

    private fun putProduct(
        productTitle: String,
        categoryId: Int,
        emojiSearchResult: EmojiSearchResult?,
    ) {
        val product = Product(
            id = productId ?: 0,
            title = productTitle.replaceFirstChar { it.uppercaseChar() },
            emojiSearchResult = emojiSearchResult,
            categoryId = categoryId,
            enabled = true,
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.putProduct(product)
        }
    }

    private fun finalizeInput() {
        if (productId == null) {
            title.value = TextFieldValue()
            explicitlySelectedCategory.value = null
            events.trySend(Event.ProductAdded)
        } else {
            events.trySend(Event.Completed)
        }
    }

    private fun handleCategoryNotSpecified() {
        categoryPickerExpanded.value = true
        events.trySend(Event.CategoryNotSpecified)
    }

    override fun onCategoryPickerExpandChange(expanded: Boolean) {
        categoryPickerExpanded.value = expanded
    }

    override fun onCategorySelected(category: CategoryProps) {
        explicitlySelectedCategory.value = category
        categoryPickerExpanded.value = false
    }

    override fun onComplete() {
        events.trySend(Event.Completed)
    }

    fun title(): StateFlow<TextFieldValue> =
        title

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        Completed,
        ProductAdded,
        CategoryNotSpecified,
        TitleNotSpecified,
    }

    @AssistedFactory
    fun interface Factory {
        fun create(productId: Int?): ProductInputFormViewModel
    }
}
