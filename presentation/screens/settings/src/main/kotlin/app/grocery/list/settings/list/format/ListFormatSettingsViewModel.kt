package app.grocery.list.settings.list.format

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ListFormatSettingsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val productItemFormatMapper: ProductItemFormatMapper,
) : ViewModel(),
    ListFormatSettingsCallbacks {

    val props = repository
        .productItemFormat()
        .map { format ->
            ListFormatSettingsProps(
                productItemFormat = productItemFormatMapper.toPresentation(format),
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override fun onProductListFormatSelected(option: ListFormatSettingsProps.ProductItemFormat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setProductItemFormat(
                productItemFormatMapper.toDomain(option)
            )
        }
    }
}
