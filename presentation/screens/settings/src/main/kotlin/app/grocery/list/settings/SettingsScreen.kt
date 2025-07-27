package app.grocery.list.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.grocery.list.commons.compose.elements.AppHorizontalDividerWithOffset
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
    navController: NavHostController,
) {
    settings(navController)
    listFormatSettings()
}

private fun NavGraphBuilder.settings(navController: NavHostController) {
    composable<Settings> {
        SettingsScreen(
            navController = navController,
        )
    }
}

@Composable
private fun SettingsScreen(
    navController: NavHostController,
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
                navController.navigate(ListFormatSettings)
            },
        )
        AppHorizontalDividerWithOffset()
    }
}

@PreviewLightDark
@Composable
private fun SettingsScreenPreview() {
    GroceryListTheme {
        Scaffold { padding ->
            SettingsScreen(
                navController = rememberNavController(),
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
