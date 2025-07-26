package app.grocery.list.settings.notification

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
internal class NotificationSettingsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val itemInNotificationModeMapper: ItemInNotificationModeMapper,
) : ViewModel(),
    NotificationSettingsCallbacks {

    val props = repository
        .itemInNotificationMode()
        .map { itemInNotificationMode ->
            NotificationSettingsProps(
                itemInNotificationMode = itemInNotificationModeMapper.toPresentation(itemInNotificationMode),
            )
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null,
        )

    override fun onItemInNotificationModeSelected(mode: NotificationSettingsProps.ItemInNotificationMode) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setItemInNotificationMode(
                itemInNotificationModeMapper.toDomain(mode)
            )
        }
    }
}
