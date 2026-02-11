package app.grocery.list.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.ScrollableContentWithShadows
import app.grocery.list.commons.compose.theme.AppIcons
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.dialog.SettingsDialog
import app.grocery.list.settings.dialog.SettingsDialogProps
import app.grocery.list.settings.elements.menu.item.SettingsMenuItem
import commons.android.browse
import commons.android.email
import kotlinx.coroutines.channels.ReceiveChannel

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
    val dialog by viewModel.dialog.collectAsState()
    EventConsumer(
        events = viewModel.events(),
        callbacks = viewModel,
        navigation = navigation,
        delegate = delegate,
    )
    SettingsScreen(
        props = props,
        navigation = navigation,
        callbacks = viewModel,
    )
    OptionalSettingsDialog(
        dialog = dialog,
        callbacks = viewModel,
    )
}

@Composable
private fun EventConsumer(
    events: ReceiveChannel<SettingsViewModel.Event>,
    navigation: SettingsNavigation,
    delegate: SettingsDelegate,
    callbacks: SettingsCallbacks,
) {
    val context = LocalContext.current
    EventConsumer(events) { event ->
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
            SettingsViewModel.Event.OnPrivacyPolicyClick -> {
                context.browse(
                    url = "https://darkharov.github.io/grocery-list-privacy-policy/",
                    onBrowserAppNotFound = {
                        callbacks.onBrowserAppNotFound()
                    },
                )
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
    val scrollState = rememberScrollState()
    ScrollableContentWithShadows(
        scrollableState = scrollState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(horizontal = dimensionResource(R.dimen.margin_0_16_48))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            SettingsMenuItem(
                leadingIcon = painterResource(R.drawable.ic_list_item_format),
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.list_format),
                ),
                onClick = {
                    callbacks.onListFormatClick()
                },
            )
            SettingsMenuItem(
                leadingIcon = painterResource(R.drawable.ic_bottom_bar),
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.bottom_bar),
                ),
                onClick = {
                    navigation.goToBottomBarSettings()
                },
            )
            SettingsMenuItem(
                leadingIcon = rememberVectorPainter(AppIcons.questionMark),
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.faq),
                ),
                onClick = {
                    callbacks.onFaqClick()
                },
            )
            SettingsMenuItem(
                leadingIcon = rememberVectorPainter(AppIcons.support),
                text = StringValue.StringWrapper(
                    value = stringResource(R.string.contact_support),
                ),
                onClick = {
                    callbacks.onContactSupportClick()
                },
            )
            SettingsMenuItem(
                text = StringValue.ResId(R.string.privacy_policy),
                leadingIcon = rememberVectorPainter(AppIcons.privacy),
                onClick = {
                    callbacks.onPrivacyPolicyClick()
                },
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
                    .padding(
                        top = 80.dp,
                        bottom = 12.dp,
                    )
                    .alpha(0.8f),
            )
        }
    }
}

@Composable
private fun OptionalSettingsDialog(
    dialog: SettingsDialogProps?,
    callbacks: SettingsCallbacks,
) {
    if (dialog != null) {
        SettingsDialog(
            props = dialog,
            callbacks = callbacks,
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
