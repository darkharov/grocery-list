package app.grocery.list.product.input.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.category.Category
import app.grocery.list.domain.category.CategoryRepository
import app.grocery.list.domain.product.AtLeastOneProductJustAddedUseCase
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.product.input.form.elements.category.picker.CategoryMapper
import app.grocery.list.product.input.form.elements.category.picker.CategoryPickerProps
import app.grocery.list.product.input.form.elements.category.picker.CategoryProps
import commons.android.customStateIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel(
    assistedFactory = ProductInputFormViewModel.Factory::class,
)
internal class ProductInputFormViewModel @AssistedInject constructor(
    @Assisted
    private val productId: Int?,
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper,
    private val emojiMapper: EmojiMapper,
    private val atLeastOneProductJustAdded: AtLeastOneProductJustAddedUseCase,
) : ViewModel(),
    ProductInputFormCallbacks {

    private val events = Channel<Event>(Channel.UNLIMITED)
    private val enabled = MutableStateFlow(true)
    private val currentEmoji = MutableStateFlow<EmojiProps?>(null)
    private val selectedCategoryId = MutableStateFlow<Int?>(null)
    private val categoryPickerExpanded = MutableStateFlow(false)

    val title = TextFieldState()
    val props = props().customStateIn(this, ProductInputFormProps())

    private fun props(): Flow<ProductInputFormProps> =
        combine(
            enabled,
            currentOrSuggestedEmoji(),
            categoryPicker(),
            atLeastOneProductJustAdded.execute(),
        ) {
                enabled,
                emoji,
                categoryPicker,
                atLeastOneProductJustAdded,
            ->
            ProductInputFormProps(
                productId = productId,
                emoji = emoji,
                enabled = enabled,
                categoryPicker = categoryPicker,
                atLeastOneProductJustAdded = atLeastOneProductJustAdded,
            )
        }

    private fun currentOrSuggestedEmoji(): Flow<EmojiProps?> =
        currentEmoji
            .flatMapLatest { current ->
                if (current != null) {
                    flowOf(current)
                } else {
                    snapshotFlow { title.text }
                        .mapLatest { title ->
                            val emoji = productRepository.findEmoji(title)
                            emojiMapper.toPresentationNullable(emoji)
                        }
                }
            }

    private fun categoryPicker(): Flow<CategoryPickerProps> =
        combine(
            categories(),
            selectedOrSuggestedCategory(),
            categoryPickerExpanded,
        ) { items, selectedOne, expanded ->
            CategoryPickerProps(
                items = items,
                expanded = expanded,
                selectedOne = categoryMapper.transformNullable(selectedOne),
            )
        }

    private fun categories(): Flow<ImmutableList<CategoryProps>> =
        categoryRepository
            .all()
            .map(categoryMapper::transformList)

    private fun selectedOrSuggestedCategory(): Flow<Category?> =
        selectedCategoryId
            .flatMapLatest { id ->
                if (id != null) {
                    flowOf(categoryRepository.get(id = id))
                } else {
                    snapshotFlow { title.text }
                        .mapLatest(categoryRepository::find)
                }
            }

    @Inject
    fun assignInitialValues() {
        viewModelScope.launch {
            if (productId != null) {
                val product = productRepository.get(id = productId)
                title.edit {
                    append(product.title)
                    placeCursorAtEnd()
                }
                enabled.value = product.enabled
                currentEmoji.value = emojiMapper.toPresentationNullable(product.emojiAndKeyword)
                selectedCategoryId.value = product.categoryId
            }
        }
    }

    override fun onAttemptToCompleteProductInput(
        productTitle: String,
        props: ProductInputFormProps,
    ) {
        val selectedCategoryId = props.selectedCategoryId
        if (productTitle.isNotBlank()) {
            if (selectedCategoryId != null) {
                putProduct(
                    productId = props.productId ?: 0,
                    productTitle = productTitle,
                    emoji = emojiMapper.toDomainNullable(props.emoji),
                    categoryId = selectedCategoryId,
                    enabled = props.enabled,
                )
                finalizeInput()
            } else {
                categoryPickerExpanded.value = true
                events.trySend(Event.CategoryNotSpecified)
            }
        } else {
            if (selectedCategoryId != null) {
                events.trySend(Event.TitleNotSpecified)
            } else if (props.atLeastOneProductJustAdded) {
                events.trySend(Event.Completed)
            } // else form is completely empty, so there's nothing to do
        }
    }

    private fun putProduct(
        productId: Int,
        productTitle: String,
        emoji: EmojiAndKeyword?,
        categoryId: Int,
        enabled: Boolean,
    ) {
        val product = Product(
            id = productId,
            title = productTitle.replaceFirstChar { it.uppercaseChar() },
            emojiAndKeyword = emoji,
            categoryId = categoryId,
            enabled = enabled,
            customListId = null,
        )
        viewModelScope.launch {
            productRepository.put(product)
        }
    }

    private fun finalizeInput() {
        if (productId == null) {
            title.edit {
                delete(0, length)
            }
            selectedCategoryId.value = null
            events.trySend(Event.ProductAdded)
        } else {
            events.trySend(Event.Completed)
        }
    }

    override fun onCategoryPickerExpandChange(expanded: Boolean) {
        categoryPickerExpanded.value = expanded
    }

    override fun onCategorySelected(categoryId: Int) {
        selectedCategoryId.value = categoryId
        categoryPickerExpanded.value = false
        events.trySend(Event.CategoryExplicitlySelected)
    }

    override fun onComplete() {
        events.trySend(Event.Completed)
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        Completed,
        ProductAdded,
        CategoryNotSpecified,
        TitleNotSpecified,
        CategoryExplicitlySelected,
    }

    @AssistedFactory
    fun interface Factory {
        fun create(productId: Int?): ProductInputFormViewModel
    }
}