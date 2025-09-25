package app.grocery.list.settings.list.format

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.commons.format.GetProductTitleFormatter
import app.grocery.list.domain.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ListFormatSettingsViewModel @Inject constructor(
    getProductTitleFormatter: GetProductTitleFormatter,
    private val repository: AppRepository,
    private val productTitleFormatMapper: ProductTitleFormatMapper,
) : ViewModel(),
    ListFormatSettingsCallbacks {

    val props =
        combine(
            repository.productTitleFormat.observe(),
            repository.sampleProducts(),
            getProductTitleFormatter.execute(),
        ) { format, sampleProducts, formatter ->
            ListFormatSettingsProps(
                productTitleFormat = productTitleFormatMapper.toPresentation(format),
                sampleOfNotificationTitle = formatter.printToString(sampleProducts),
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override fun onProductTitleFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.productTitleFormat.set(
                productTitleFormatMapper.toDomain(option)
            )
        }
    }
}
