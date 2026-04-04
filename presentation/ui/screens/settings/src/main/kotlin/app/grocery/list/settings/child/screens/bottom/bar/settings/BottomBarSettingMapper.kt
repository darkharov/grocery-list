package app.grocery.list.settings.child.screens.bottom.bar.settings

import app.grocery.list.domain.settings.BottomBarSetting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BottomBarSettingMapper @Inject constructor() {

    fun toPresentation(value: BottomBarSetting) =
        BottomBarSettingsProps(
            useIcons = (value == BottomBarSetting.IconsModeIsExplicitlySelected),
        )
}
