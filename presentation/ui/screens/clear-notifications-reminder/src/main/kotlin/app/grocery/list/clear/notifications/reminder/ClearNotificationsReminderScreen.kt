package app.grocery.list.clear.notifications.reminder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderViewModel.Event
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.elements.AppPreloader
import app.grocery.list.commons.compose.elements.button.AppButtonNext
import app.grocery.list.commons.compose.elements.titled.switch_.AppSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue

@Composable
fun ClearNotificationsReminderScreen(
    contract: ClearNotificationsReminderContract,
) {
    val viewModel = hiltViewModel<ClearNotificationsReminderViewModel>()
    val props by viewModel.props.collectAsState()
    EventConsumer(viewModel.events()) { event ->
        when (event) {
            Event.Next -> {
                contract.goToFinalSteps()
            }
        }
    }
    PreloaderOrContent(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun PreloaderOrContent(
    props: ClearNotificationsReminderProps?,
    callbacks: ClearNotificationsReminderCallbacks,
) {
    if (props == null) {
        AppPreloader()
    } else {
        Content(
            props = props,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun Content(
    props: ClearNotificationsReminderProps,
    callbacks: ClearNotificationsReminderCallbacks,
    modifier: Modifier = Modifier,
) {
    AppContentToRead(
        modifier = modifier,
        title = stringResource(R.string.advice),
        content = {
            Text(
                text = stringResource(R.string.remove_unnecessary_notifications),
                fontSize = 16.sp,
            )
        },
        imageId = R.drawable.clear_notifications,
        footer = {
            Column(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.margin_16_32_64))
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                AppSwitch(
                    text = StringValue.ResId(R.string.do_not_show_this_screen_again),
                    checked = props.doNotShowAgain,
                    onCheckedChange = { newValue ->
                        callbacks.onDoNotShowAgainCheckedChange(newValue)
                    },
                )
                Spacer(
                    modifier = Modifier
                        .height(8.dp),
                )
                AppButtonNext(
                    onClick = {
                        callbacks.onNext()
                    },
                )
                Spacer(
                    modifier = Modifier
                        .height(12.dp),
                )
            }
        },
    )
}

@Composable
@PreviewLightDark
private fun ClearNotificationsReminderScreenPreview(
    @PreviewParameter(
        provider = ClearNotificationsReminderMock::class,
    )
    props: ClearNotificationsReminderProps,
) {
    GroceryListTheme {
        Scaffold { padding ->
            Content(
                props = props,
                callbacks = ClearNotificationsReminderCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
