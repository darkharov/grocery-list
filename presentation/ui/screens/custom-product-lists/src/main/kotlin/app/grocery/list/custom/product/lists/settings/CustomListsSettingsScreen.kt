package app.grocery.list.custom.product.lists.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.elements.AppExplanationImage
import app.grocery.list.commons.compose.elements.AppPreloaderOrContent
import app.grocery.list.commons.compose.elements.switch_.AppTitledSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.theme.LocalAppColors
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
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(
                horizontal = dimensionResource(R.dimen.margin_16_32_64),
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f),
        ) {
            AppExplanationImage(
                imageId = if (props.featureEnabled) {
                    R.drawable.home_screen_with_custom_lists_icon
                } else {
                    R.drawable.home_screen
                },
                modifier = Modifier
                    .padding(vertical = 32.dp),
            )
        }
        AppTitledSwitch(
            text = StringValue.ResId(R.string.enable_multiple_lists),
            checked = props.featureEnabled,
            onCheckedChange = { newValue ->
                callbacks.onCustomListsEnabledChange(newValue)
            },
            description = StringValue.ResId(R.string.multiple_lists_icon_explanation),
            descriptionAlpha = if (props.featureEnabled) 1f else 0f,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 36.dp),
        )
    }
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
            modifier = Modifier
                .background(LocalAppColors.current.background),
        )
    }
}
