package app.grocery.list.custom.product.lists.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.elements.AppPreloaderOrContent
import app.grocery.list.commons.compose.elements.AppSimpleSettingLayout
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.custom.product.lists.R

@Composable
fun CustomListsSettingsScreen() {
    val viewModel = hiltViewModel<CustomListsSettingsViewModel>()
    val props by viewModel.props.collectAsStateWithLifecycle()
    AppPreloaderOrContent(props = props) { props ->
        CustomListsSettingsScreen(
            props = props,
            callbacks = viewModel
        )
    }
}

@Composable
private fun CustomListsSettingsScreen(
    props: CustomListsSettingsProps,
    callbacks: CustomListsSettingsCallbacks,
) {
    AppSimpleSettingLayout(
        checked = props.featureEnabled,
        imageId = if (props.featureEnabled) {
            R.drawable.home_screen_with_custom_lists_icon
        } else {
            R.drawable.home_screen
        },
        text = StringValue.ResId(R.string.use_multiple_lists),
        explanation = StringValue.ResId(R.string.multiple_lists_icon_explanation),
        onCheckedChange = { newValue ->
            callbacks.onCustomListsEnabledChange(newValue)
        }
    )
}

@PreviewLightDark
@Composable
private fun CustomListsSettingsScreenPreview(
    @PreviewParameter(
        provider = CustomListsSettingsMocks::class,
    )
    props: CustomListsSettingsProps,
) {
    GroceryListTheme {
        CustomListsSettingsScreen(
            props = props,
            callbacks = CustomListsSettingsCallbacksMock,
        )
    }
}
