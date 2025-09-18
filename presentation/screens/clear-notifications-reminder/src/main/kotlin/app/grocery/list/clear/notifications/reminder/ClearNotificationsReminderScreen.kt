package app.grocery.list.clear.notifications.reminder

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import app.grocery.list.clear.notifications.reminder.ClearNotificationsReminderViewModel.Event
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppContentToRead
import app.grocery.list.commons.compose.elements.button.AppButton
import app.grocery.list.commons.compose.elements.button.AppButtonProps
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

    EventConsumer(viewModel.events()) { event ->
        when (event) {
            Event.Next -> {
                navigation.goToFinalSteps()
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
            fontSize = 14.sp,
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
