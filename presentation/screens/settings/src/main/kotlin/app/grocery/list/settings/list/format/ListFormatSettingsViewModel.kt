package app.grocery.list.settings.list.format

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.AppRepository
import app.grocery.list.domain.format.ProductListSeparator
import app.grocery.list.domain.format.printToString
import app.grocery.list.domain.product.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ListFormatSettingsViewModel @Inject constructor(
    productRepository: ProductRepository,
    private val appRepository: AppRepository,
    private val productTitleFormatMapper: ProductTitleFormatMapper,
) : ViewModel(),
    ListFormatSettingsCallbacks {

    val props =
        combine(
            appRepository.productTitleFormatter.observe(),
            productRepository.sampleProducts(),
        ) { formatter, sampleProducts ->
            ListFormatSettingsProps(
                productTitleFormat = productTitleFormatMapper.toPresentation(formatter),
                sampleOfNotificationTitle = formatter
                    .printToString(
                        products = sampleProducts,
                        separator = ProductListSeparator.Notifications,
                    ),
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override fun onProductTitleFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.productTitleFormatter.set(
                productTitleFormatMapper.toDomain(option)
            )
        }
    }
}
