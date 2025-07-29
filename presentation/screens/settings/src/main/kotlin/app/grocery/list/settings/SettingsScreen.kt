package app.grocery.list.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.list.format.ListFormatSettings
import app.grocery.list.settings.list.format.listFormatSettings
import kotlinx.serialization.Serializable

@Serializable
data object Settings

fun NavGraphBuilder.settingsAndChildScreens(
    delegate: SettingsDelegate,
    navController: NavHostController,
) {
    settings(
        delegate = delegate,
        navController = navController,
    )
    listFormatSettings()
}

private fun NavGraphBuilder.settings(
    delegate: SettingsDelegate,
    navController: NavHostController,
) {
    composable<Settings> {
        val viewModel = hiltViewModel<SettingsViewModel>()
        EventConsumer(
            viewModel = viewModel,
            navController = navController,
            delegate = delegate,
        )
        SettingsScreen(
            callbacks = viewModel,
        )
    }
}

@Composable
private fun EventConsumer(
    viewModel: SettingsViewModel,
    navController: NavHostController,
    delegate: SettingsDelegate,
) {
    EventConsumer(
        key = viewModel,
        events = viewModel.events(),
    ) { event ->
        when (event) {
            SettingsViewModel.Event.OnContactSupport -> {
                delegate.contactSupport()
            }
            SettingsViewModel.Event.OnGoToListFormatSettings -> {
                navController.navigate(ListFormatSettings)
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    callbacks: SettingsCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        AppTextButton(
            props = AppTextButtonProps.SettingsCategory(
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.list_format),
                ),
                leadingIconId = R.drawable.ic_list_format,
            ),
            onClick = {
                callbacks.onListFormatClick()
            },
        )
        AppTextButton(
            props = AppTextButtonProps.SettingsCategory(
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.contact_support),
                ),
                leadingIconId = R.drawable.ic_support,
            ),
            onClick = {
                callbacks.onContactSupportClick()
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            SettingsScreen(
                callbacks = SettingsCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
