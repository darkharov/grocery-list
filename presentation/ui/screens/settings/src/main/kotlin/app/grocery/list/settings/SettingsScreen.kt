package app.grocery.list.settings

import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.button.text.AppTextButton
import app.grocery.list.commons.compose.elements.button.text.AppTextButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import commons.android.email

@Composable
fun SettingsScreen(
    delegate: SettingsDelegate,
    navigation: SettingsNavigation,
) {
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
        navigation = navigation,
        delegate = delegate,
    )
    SettingsScreen(
        props = props,
        navigation = navigation,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    viewModel: SettingsViewModel,
    navigation: SettingsNavigation,
    delegate: SettingsDelegate,
) {
    val context = LocalContext.current
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            SettingsViewModel.Event.OnContactSupport -> {
                context.email(
                    email = "product.list.supp@gmail.com",
                    subject = "Android, app.grocery.list",
                    text =
                        "\n\n\n\n" +
                        "\nAndroid version: ${Build.VERSION.RELEASE} " +
                        "(API level ${Build.VERSION.SDK_INT})" +
                        "\nVersion Code: ${delegate.appVersionCode}" +
                        "\nVersion Name: ${delegate.appVersionName}" +
                        "\nBrand: ${Build.BRAND}" +
                        "\nModel: ${Build.MODEL}",
                )
            }
            SettingsViewModel.Event.OnGoToListFormatSettings -> {
                navigation.goToListFormatSettings()
            }
            SettingsViewModel.Event.OnFaqClick -> {
                navigation.goToFaq()
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    props: SettingsProps,
    callbacks: SettingsCallbacks,
    navigation: SettingsNavigation,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_0_16_48))
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
                navigation.goToBottomBarSettings()
            },
        )
        AppTextButton(
            props = AppTextButtonProps.SettingsCategory(
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.faq),
                ),
                leadingIconId = R.drawable.ic_question_mark,
            ),
            onClick = {
                callbacks.onFaqClick()
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
                navigation = SettingsNavigationMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
