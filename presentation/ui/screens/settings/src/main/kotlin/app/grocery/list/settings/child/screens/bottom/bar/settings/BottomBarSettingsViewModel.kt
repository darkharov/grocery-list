package app.grocery.list.settings.child.screens.bottom.bar.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.grocery.list.domain.settings.BottomBarSetting
import app.grocery.list.domain.settings.SettingsRepository
import commons.android.customStateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
internal class BottomBarSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val bottomBarSettingMapper: BottomBarSettingMapper,
) : ViewModel(),
    BottomBarSettingsCallbacks {

    val props: StateFlow<BottomBarSettingsProps?> =
        settingsRepository
            .bottomBarSetting
            .observe()
            .map(bottomBarSettingMapper::toPresentation)
            .customStateIn(this)

    override fun onCheckedChange(newValue: Boolean) {
        viewModelScope.launch {
            settingsRepository.bottomBarSetting.set(
                if (newValue) {
                    BottomBarSetting.IconsModeIsExplicitlySelected
                } else {
                    BottomBarSetting.ButtonsIsExplicitlySelected
                }
            )
        }
    }
}
