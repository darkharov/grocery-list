package app.grocery.list.settings.child.screens.list.format

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.notification.GetNotificationSampleUseCase
import app.grocery.list.domain.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class ListFormatSettingsViewModel @Inject constructor(
    getNotificationSample: GetNotificationSampleUseCase,
    private val settingsRepository: SettingsRepository,
    private val productTitleFormatMapper: ProductTitleFormatMapper,
) : ViewModel(),
    ListFormatSettingsCallbacks {

    val props =
        getNotificationSample
            .execute()
            .map { notificationSample ->
                ListFormatSettingsProps(
                    productTitleFormat = productTitleFormatMapper.toPresentation(notificationSample.format),
                    sampleOfNotificationTitle = notificationSample.notificationText,
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                null,
            )

    override fun onProductTitleFormatSelected(option: ListFormatSettingsProps.ProductTitleFormat) {
        viewModelScope.launch {
            settingsRepository.productTitleFormat.set(
                productTitleFormatMapper.toDomain(option)
            )
        }
    }
}
