package app.grocery.list.custom.lists.picker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.custom.lists.picker.item.ProductListPickerItemProps
import app.grocery.list.custom.lists.picker.item.mappers.ProductListIdMapper
import app.grocery.list.domain.product.list.ProductListRepository
import app.grocery.list.domain.product.list.SummarizeProductListsUseCase
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class ProductListPickerViewModel @Inject constructor(
    pickerMapper: ProductListPickerMapper,
    summarizeProductLists: SummarizeProductListsUseCase,
    private val productListIdMapper: ProductListIdMapper,
    private val productListRepository: ProductListRepository,
) : ViewModel(),
    ProductListPickerCallbacks {

    val props = summarizeProductLists
        .execute()
        .map(pickerMapper::toPresentation)
        .customStateIn(this)

    override fun onSelect(item: ProductListPickerItemProps) {
        viewModelScope.launch {
            productListRepository.setSelectedOne(
                productListIdMapper.toDomain(itemId = item.id)
            )
        }
    }
}
