package app.grocery.list.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.bottom.bar.BottomBarSettings
import app.grocery.list.settings.bottom.bar.bottomBarSettings
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
    bottomBarSettings(
        ableToGoBack = navController::popBackStack,
    )
}

private fun NavGraphBuilder.settings(
    delegate: SettingsDelegate,
    navController: NavHostController,
) {
    composable<Settings> {
        val viewModel = hiltViewModel<SettingsViewModel, SettingsViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(
                    appVersionName = delegate.appVersionName,
                )
            }
        )
        val props by viewModel.props.collectAsState()
        EventConsumer(
            viewModel = viewModel,
            navController = navController,
            delegate = delegate,
        )
        SettingsScreen(
            props = props,
            navController = navController,
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
    EventConsumer(viewModel.events()) { event ->
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
    props: SettingsProps,
    callbacks: SettingsCallbacks,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppTextButton(
            props = AppTextButtonProps.SettingsCategory(
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.list_format),
                ),
                leadingIconId = R.drawable.ic_list_item_format,
            ),
            onClick = {
                callbacks.onListFormatClick()
            },
        )
        AppTextButton(
            props = AppTextButtonProps.SettingsCategory(
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.bottom_bar),
                ),
                leadingIconId = R.drawable.ic_bottom_bar,
            ),
            onClick = {
                navController.navigate(BottomBarSettings)
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
        Spacer(
            modifier = Modifier
                .weight(1f),
        )
        Text(
            text = props.appVersionName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets
                        .systemBars
                        .only(WindowInsetsSides.Bottom),
                )
                .padding(bottom = 12.dp)
                .alpha(0.8f),
        )
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            SettingsScreen(
                props = SettingsProps(appVersionName = "1.0.0"),
                callbacks = SettingsCallbacksMock,
                navController = rememberNavController(),
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
