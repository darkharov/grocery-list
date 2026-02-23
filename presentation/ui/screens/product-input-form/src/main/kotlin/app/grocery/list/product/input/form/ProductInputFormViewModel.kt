package app.grocery.list.product.input.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuMapper
import app.grocery.list.commons.compose.elements.dropdown.menu.AppDropdownMenuProps
import app.grocery.list.domain.category.Category
import app.grocery.list.domain.category.CategoryRepository
import app.grocery.list.domain.product.AtLeastOneProductJustAddedUseCase
import app.grocery.list.domain.product.EmojiAndKeyword
import app.grocery.list.domain.product.Product
import app.grocery.list.domain.product.ProductRepository
import app.grocery.list.domain.product.PutProductFromInputFormUseCase
import app.grocery.list.domain.product.list.GetProductListsIfFeatureEnabledUseCase
import app.grocery.list.domain.product.list.ProductList
import app.grocery.list.kotlin.customCombine
import app.grocery.list.product.input.form.mappers.CategoryDropdownMenuItemMapper
import app.grocery.list.product.input.form.mappers.EmojiMapper
import app.grocery.list.product.input.form.mappers.ProductInputFormPropsMapper
import app.grocery.list.product.input.form.mappers.ProductListDropdownMenuItemMapper
import commons.android.customStateIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
    private val getProductListsIfFeatureEnabled: GetProductListsIfFeatureEnabledUseCase,
    private val putProductFromInputForm: PutProductFromInputFormUseCase,
    private val categoryRepository: CategoryRepository,
    private val emojiMapper: EmojiMapper,
    private val categoryMapper: CategoryDropdownMenuItemMapper,
    private val productListMapper: ProductListDropdownMenuItemMapper,
    private val atLeastOneProductJustAdded: AtLeastOneProductJustAddedUseCase,
    private val productInputFormPropsMapper: ProductInputFormPropsMapper,
) : ViewModel(), ProductInputFormCallbacks {

    private val events = Channel<Event>(Channel.UNLIMITED)
    private val enabled = MutableStateFlow(true)
    private val currentEmoji = MutableStateFlow<EmojiAndKeyword?>(null)
    private val selectedCategoryId = MutableStateFlow<Int?>(null)
    private val categoryPickerExpanded = MutableStateFlow(false)
    private val selectedProductList = MutableStateFlow<ProductList?>(null)
    private val productListExpanded = MutableStateFlow(false)

    val title = TextFieldState()
    val props = props().customStateIn(this, ProductInputFormProps(productId = productId))

    private fun props(): Flow<ProductInputFormProps> =
        customCombine(
            flowOf(productId),
            enabled,
            currentOrSuggestedEmoji(),
            combine(
                categoryRepository.all(),
                selectedOrSuggestedCategory(),
                categoryPickerExpanded,
                AppDropdownMenuMapper<Category>::Params,
            ),
            productListDropDownParams(),
            atLeastOneProductJustAdded.execute(),
            snapshotFlow { title.text },
            ProductInputFormPropsMapper::Params,
        ).map(productInputFormPropsMapper::transform)

    private fun currentOrSuggestedEmoji(): Flow<EmojiAndKeyword?> =
        currentEmoji
            .flatMapLatest { current ->
                if (current != null) {
                    flowOf(current)
                } else {
                    snapshotFlow { title.text }
                        .mapLatest { title ->
                            productRepository.findEmoji(title)
                        }
                }
            }

    private fun selectedOrSuggestedCategory(): Flow<Category?> =
        selectedCategoryId
            .flatMapLatest { id ->
                if (id != null) {
                    flowOf(categoryRepository.get(id = id))
                } else {
                    snapshotFlow { title.text }
                        .mapLatest { title ->
                            categoryRepository.find(search = title)
                        }
                }
            }

    private fun productListDropDownParams(): Flow<AppDropdownMenuMapper.Params<ProductList>?> =
        combine(
            getProductListsIfFeatureEnabled.execute(),
            productListExpanded,
            selectedProductList,
        ) { productLists, productListExpanded, explicitlySelected ->
            if (productLists != null) {
                AppDropdownMenuMapper.Params(
                    items = productLists.productLists,
                    selectedOne = explicitlySelected ?: productLists.selectedOne,
                    expanded = productListExpanded,
                )
            } else {
                null
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
                currentEmoji.value = product.emojiAndKeyword
                selectedCategoryId.value = product.categoryId
            }
        }
    }

    override fun onAttemptToCompleteProductInput(
        productTitle: String,
        props: ProductInputFormProps,
    ) {
        val selectedCategory = categoryMapper.toDomainNullable(props.categoriesDropdown.selectedOne)
        val productLists = props.productListsDropdown
        val idOfSelectedProductList = if (productLists != null) {
            val selection = props.productListsDropdown.selectedOne
            val mappedSelection = productListMapper.toDomainNullable(selection)
            mappedSelection?.id ?: throw IllegalStateException("Product list must be specified")
        } else {
            ProductList.Id.Default
        }

        if (productTitle.isBlank()) {
            if (selectedCategory != null) {
                events.trySend(Event.TitleNotSpecified)
            } else if (props.atLeastOneProductJustAdded) {
                events.trySend(Event.Completed)
            } // else form is completely empty, so there's nothing to do
            return
        }

        if (selectedCategory == null) {
            categoryPickerExpanded.value = true
            events.trySend(Event.CategoryNotSpecified)
            return
        }

        val product = Product(
            id = productId ?: 0,
            title = productTitle.replaceFirstChar { it.uppercaseChar() },
            emojiAndKeyword = emojiMapper.toDomainNullable(props.emoji),
            categoryId = selectedCategory.id,
            enabled = props.enabled,
            productListId = idOfSelectedProductList,
        )

        viewModelScope.launch {
            putProductFromInputForm.execute(product)
            finalizeInput()
        }
    }

    private fun finalizeInput() {
        if (productId == null) {
            title.edit {
                delete(0, length)
            }
            selectedCategoryId.value = null
            selectedProductList.value = null
            events.trySend(Event.ProductAdded)
        } else {
            events.trySend(Event.Completed)
        }
    }

    override fun onCategoriesExpandedChange(expanded: Boolean) {
        categoryPickerExpanded.value = expanded
    }

    override fun onCategorySelected(category: AppDropdownMenuProps.Item) {
        selectedCategoryId.value = categoryMapper.toDomain(category).id
        categoryPickerExpanded.value = false
        events.trySend(Event.FocusOnTitle)
    }

    override fun onProductListsExpandedChange(expanded: Boolean) {
        productListExpanded.value = expanded
    }

    override fun onProductListSelected(productList: AppDropdownMenuProps.Item) {
        selectedProductList.value = productListMapper.toDomain(productList)
        productListExpanded.value = false
        events.trySend(Event.FocusOnTitle)
    }

    override fun onDone(productTitle: String, props: ProductInputFormProps) {
        if (productId == null) {
            events.trySend(Event.Completed)
        } else {
            onAttemptToCompleteProductInput(
                productTitle = productTitle,
                props = props,
            )
        }
    }

    fun events(): ReceiveChannel<Event> =
        events

    enum class Event {
        Completed,
        ProductAdded,
        CategoryNotSpecified,
        TitleNotSpecified,
        FocusOnTitle,
    }

    @AssistedFactory
    fun interface Factory {
        fun create(productId: Int?): ProductInputFormViewModel
    }
}
