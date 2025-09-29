package app.grocery.list.commons.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

@Composable
fun <E> EventConsumer(
    events: ReceiveChannel<E>,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    onEvent: suspend (E) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(events, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(lifecycleState) {
            for (event in events) {
                onEvent(event)
            }
        }
    }
}

@Composable
fun KeyboardOnComposition(
    focusRequester: FocusRequester,
) {
    KeyboardKeeper(
        focusRequester = focusRequester,
        once = true,
    )
}

@Suppress("SameParameterValue")
@Composable
private fun KeyboardKeeper(
    focusRequester: FocusRequester,
    once: Boolean,
) {
    val windowInfo = LocalWindowInfo.current
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        snapshotFlow { windowInfo.isWindowFocused }
            .filter { isWindowFocused -> isWindowFocused }
            .take(if (once) 1 else Int.MAX_VALUE)
            .collect {
                focusRequester.requestFocus()
                keyboard?.show()
                return@collect
            }
    }
}
