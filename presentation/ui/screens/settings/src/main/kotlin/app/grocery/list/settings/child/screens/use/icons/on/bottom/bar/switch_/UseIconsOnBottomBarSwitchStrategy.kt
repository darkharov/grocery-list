package app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_

import androidx.compose.runtime.Immutable
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.R

@Immutable
enum class UseIconsOnBottomBarSwitchStrategy(
    val description: StringValue?,
    val shouldExitIfToggledOn: Boolean,
    val shouldShowExplanationImage: Boolean,
    val shouldShowCloseIcon: Boolean,
) {
    Screen(
        description = null,
        shouldExitIfToggledOn = false,
        shouldShowExplanationImage = true,
        shouldShowCloseIcon = false,
    ),
    EmbeddedElement(
        description = StringValue.ResId(R.string.this_action_can_be_cancelled_in_app_settings),
        shouldExitIfToggledOn = true,
        shouldShowExplanationImage = false,
        shouldShowCloseIcon = true,
    ),
    ;
}
