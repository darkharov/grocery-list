package app.grocery.list.product.list.actions.suggestion.to.switch_.to.icons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.grocery.list.commons.compose.elements.switch_.AppCloseableTitleSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.product.list.actions.R

@Composable
internal fun SuggestionToSwitchToIcons(
    visible: Boolean,
    callbacks: SuggestionToSwitchToIconsCallbacks,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = EnterTransition.None,
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 0,
            ),
        ) +
        shrinkVertically(
            animationSpec = tween(
                delayMillis = 20,
            ),
        ),
    ) {
        AppCloseableTitleSwitch(
            text = StringValue.ResId(R.string.use_icons_instead_of_buttons),
            explanation = StringValue.ResId(R.string.this_action_can_be_cancelled_in_app_settings),
            checked = false,
            onClose = {
                callbacks.onRefuseToSwitchToIcons()
            },
            onCheckedChange = {
                callbacks.onSwitchToIcons()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun SuggestionToSwitchToIconsPreview() {
    GroceryListTheme {
        SuggestionToSwitchToIcons(
            visible = true,
            callbacks = SuggestionToSwitchToIconsCallbacksMock,
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
