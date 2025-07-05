package app.darkharov.clear.notifications.reminder

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.darkharov.clear.notifications.reminder.ClearNotificationsReminderViewModel.Event
import app.grocery.list.clear.notifications.reminder.R
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppExplanationImage
import app.grocery.list.commons.compose.elements.AppSimpleLayout
import app.grocery.list.commons.compose.elements.app.button.AppButton
import app.grocery.list.commons.compose.elements.app.button.AppButtonProps
import app.grocery.list.commons.compose.theme.GroceryListTheme
import kotlinx.serialization.Serializable

@Serializable
data object ClearNotificationsReminder

fun NavGraphBuilder.clearNotificationsReminder(
    navigation: ClearNotificationsReminderNavigation,
) {
    composable<ClearNotificationsReminder> {
        ClearNotificationsReminderScreen(navigation)
    }
}

@Composable
private fun ClearNotificationsReminderScreen(
    navigation: ClearNotificationsReminderNavigation,
) {
    val viewModel = hiltViewModel<ClearNotificationsReminderViewModel>()
    val props by viewModel.props.collectAsState()

    EventConsumer(
        key = viewModel,
        events = viewModel.events(),
        lifecycleState = Lifecycle.State.RESUMED,
    ) { event ->
        when (event) {
            Event.Next -> {
                navigation.goToPreparingForShopping()
            }
        }
    }

    ClearNotificationsReminderScreen(
        props = props,
        callbacks = viewModel,
    )
}

@Composable
private fun ClearNotificationsReminderScreen(
    props: ClearNotificationsReminderProps,
    callbacks: ClearNotificationsReminderCallbacks,
    modifier: Modifier = Modifier,
) {
    AppSimpleLayout(
        modifier = modifier,
        scrollableContent = {
            Spacer(
                modifier = Modifier
                    .height(40.dp),
            )
            Text(
                text = stringResource(R.string.remove_unnecessary_notifications),
                fontSize = 16.sp,
            )
            AppExplanationImage(
                painter = painterResource(R.drawable.clear_notifications),
                modifier = Modifier
                    .padding(
                        vertical = 32.dp,
                    ),
            )
        },
        footer = {
            Column {
                DoNotShowAgain(
                    props = props,
                    callbacks = callbacks,
                )
                Spacer(
                    modifier = Modifier
                        .height(8.dp),
                )
                AppButton(
                    props = AppButtonProps.Next(
                        state = AppButtonProps.State.progress(
                            progress = props.progress,
                        ),
                    ),
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
private fun DoNotShowAgain(
    props: ClearNotificationsReminderProps,
    callbacks: ClearNotificationsReminderCallbacks,
    modifier: Modifier = Modifier,
) {
    val shape = MaterialTheme.shapes.small
    Row(
        modifier = modifier
            .clip(shape)
            .clickable {
                callbacks.onDoNotShowAgainCheckedChange(newValue = !(props.doNotShowAgain))
            }
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shape,
            )
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.do_not_show_this_screen_again),
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f),
        )
        Spacer(
            modifier = Modifier
                .padding(8.dp),
        )
        Switch(
            checked = props.doNotShowAgain,
            onCheckedChange = null,
        )
    }
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
            ClearNotificationsReminderScreen(
                props = props,
                callbacks = ClearNotificationsReminderCallbacksMock,
                modifier = Modifier
                    .padding(padding),
            )
        }
    }
}
