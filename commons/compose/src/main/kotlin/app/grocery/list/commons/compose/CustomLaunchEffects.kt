package app.grocery.list.commons.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

@Composable
fun <E> EventConsumer(
    events: ReceiveChannel<E>,
    key: Any = events,
    lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED,
    onEvent: suspend (E) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key, lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(lifecycleState) {
                for (event in events) {
                    onEvent(event)
                }
            }
        }
    }
}
