package app.grocery.list.custom.lists.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.product.list.CustomListsFeatureState
import app.grocery.list.domain.product.list.CustomListsSettingsRepository
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class CustomListsSettingsViewModel @Inject constructor(
    private val repository: CustomListsSettingsRepository,
) : ViewModel(),
    CustomListsSettingsCallbacks {

    val props: StateFlow<CustomListsSettingsProps?> =
        repository
            .featureState()
            .map {
                CustomListsSettingsProps(
                    featureEnabled = (it == CustomListsFeatureState.Enabled),
                )
            }
            .customStateIn(this)

    override fun onCustomListsEnabledChange(newValue: Boolean) {
        viewModelScope.launch {
            repository.setFeatureEnabled(newValue)
        }
    }
}
