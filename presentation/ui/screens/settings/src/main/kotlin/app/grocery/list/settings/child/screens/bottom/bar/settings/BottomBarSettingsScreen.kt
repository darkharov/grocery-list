package app.grocery.list.settings.child.screens.bottom.bar.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.elements.AppPreloaderOrContent
import app.grocery.list.commons.compose.elements.AppSimpleSettingLayout
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.R

@Composable
fun BottomBarSettingsScreen() {
    val viewModel = hiltViewModel<BottomBarSettingsViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    AppPreloaderOrContent(props) { props ->
        BottomBarSettings(
            props = props,
            callbacks = viewModel,
        )
    }
}

@Composable
internal fun BottomBarSettings(
    props: BottomBarSettingsProps,
    callbacks: BottomBarSettingsCallbacks,
) {
    AppSimpleSettingLayout(
        checked = props.useIcons,
        imageId = if (props.useIcons) {
            R.drawable.home_screen_with_icons
        } else {
            R.drawable.home_screen_with_buttons
        },
        text = StringValue.ResId(R.string.use_icons_instead_of_buttons),
        onCheckedChange = { newValue ->
            callbacks.onCheckedChange(newValue = newValue)
        },
        gradientDirection = AppGradientDirection.Downward,
    )
}

@Composable
@PreviewLightDark
private fun BottomBarSettingsPreview(
    @PreviewParameter(
        provider = BottomBarSettingsMocks::class,
    )
    props: BottomBarSettingsProps,
) {
    GroceryListTheme {
        BottomBarSettings(
            props = props,
            callbacks = BottomBarSettingsCallbacksMock,
        )
    }
}
