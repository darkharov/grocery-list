package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun AppSwipeToDismissBox(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundContent: @Composable RowScope.(SwipeToDismissBoxState) -> Unit,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val state = rememberSwipeToDismissBoxState(SwipeToDismissBoxValue.Settled)
    var dismissed by rememberSaveable { mutableStateOf(false) }
    val resettingScope = rememberCoroutineScope()
    SwipeToDismissBox(
        state = state,
        backgroundContent = {
            backgroundContent(state)
        },
        modifier = modifier,
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        onDismiss = {
            dismissed = !(dismissed)
            if (dismissed) {
                onDismiss()
            } else {
                resettingScope.launch {
                    state.reset()
                }
            }
        },
        content = content,
    )
}
