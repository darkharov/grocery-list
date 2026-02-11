package app.grocery.list.settings.child.screens.use.icons.on.bottom.bar.switch_

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.grocery.list.commons.compose.AbleToGoBack
import app.grocery.list.commons.compose.AppGradientDirection
import app.grocery.list.commons.compose.EventConsumer
import app.grocery.list.commons.compose.elements.AppExplanationImage
import app.grocery.list.commons.compose.elements.button.icon.AppCloseButton
import app.grocery.list.commons.compose.elements.titled.switch_.AppSwitch
import app.grocery.list.commons.compose.theme.GroceryListTheme
import app.grocery.list.commons.compose.values.StringValue
import app.grocery.list.settings.R
import kotlinx.coroutines.channels.ReceiveChannel

private typealias VM = UseIconsOnBottomBarSwitchViewModel
private typealias VMF = UseIconsOnBottomBarSwitchViewModel.Factory

@Composable
fun UseIconsOnBottomBarSwitch(
    strategy: UseIconsOnBottomBarSwitchStrategy,
    navigation: AbleToGoBack,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<VM, VMF>(
        creationCallback = { factory ->
            factory.create(strategy)
        }
    )
    val props by viewModel.props.collectAsStateWithLifecycle()
    EventConsumer(
        events = viewModel.events(),
        navigation = navigation,
    )
    UseIconsOnBottomBarSwitch(
        props = props,
        callbacks = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun EventConsumer(
    events: ReceiveChannel<UseIconsOnBottomBarSwitchViewModel.Event>,
    navigation: AbleToGoBack,
) {
    EventConsumer(events) { event ->
        when (event) {
            is UseIconsOnBottomBarSwitchViewModel.Event.OnGoBack -> {
                navigation.goBack()
            }
        }
    }
}

@Composable
private fun UseIconsOnBottomBarSwitch(
    props: UseIconsOnBottomBarSwitchProps,
    callbacks: UseIconsOnBottomBarSwitchCallbacks,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = props.visible,
        enter = EnterTransition.None,
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 0,
            ),
        ) +
        shrinkVertically(
            animationSpec = tween(
                delayMillis = 20,
            ),
        ),
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
        ) {
            if (props.strategy.shouldShowExplanationImage) {
                AppExplanationImage(
                    imageId = if (props.checked == true) {
                        R.drawable.home_screen_with_icons
                    } else {
                        R.drawable.home_screen_with_buttons
                    },
                    modifier = Modifier
                        .alpha(
                            if (props.checked == null) {
                                0f
                            } else {
                                1f
                            }
                        ),
                    direction = AppGradientDirection.Downward,
                    gradientHeight = 120.dp,
                )
                Spacer(
                    modifier = Modifier
                        .height(32.dp),
                )
            }
            if (props.strategy.shouldShowCloseIcon) {
                AppCloseButton(
                    onClick = {
                        callbacks.onClose()
                    },
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 2.dp,
                        )
                        .align(Alignment.End)
                )
            }
            AppSwitch(
                checked = props.checked,
                text = StringValue.ResId(R.string.use_icons_instead_of_buttons),
                description = props.strategy.description,
                onCheckedChange = { newValue ->
                    callbacks.onUseIconsOnBottomBarCheckedChange(newValue = newValue)
                },
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
)
private fun UseIconsOnBottomBarSwitchPreview(
    @PreviewParameter(
        provider = UseIconsOnBottomBarSwitchMock::class,
    )
    props: UseIconsOnBottomBarSwitchProps,
) {
    GroceryListTheme {
        UseIconsOnBottomBarSwitch(
            props = props,
            callbacks = UseIconsOnBottomBarSwitchCallbacksMock,
            modifier = Modifier
                .padding(12.dp),
        )
    }
}
