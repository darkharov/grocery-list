package app.grocery.list.custom.product.lists.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.product.list.CustomProductListsFeatureSetting
import app.grocery.list.domain.product.list.ProductListRepository
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class CustomListsSettingsViewModel @Inject constructor(
    private val repository: ProductListRepository,
) : ViewModel(),
    CustomListsSettingsCallbacks {

    val props: StateFlow<CustomListsSettingsProps?> =
        repository
            .customListsFeatureSetting()
            .map {
                CustomListsSettingsProps(
                    featureEnabled = (it == CustomProductListsFeatureSetting.Enabled),
                )
            }
            .customStateIn(this)

    override fun onCustomListsEnabledChange(newValue: Boolean) {
        viewModelScope.launch {
            repository.setFeatureEnabled(newValue)
        }
    }
}
