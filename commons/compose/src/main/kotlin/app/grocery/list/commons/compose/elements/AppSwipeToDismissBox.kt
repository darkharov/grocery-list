package app.grocery.list.commons.compose.elements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun AppSwipeToDismissBox(
    key: Any,
    onSwipeFromEndToStartFinished: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundContent: @Composable RowScope.(SwipeToDismissBoxState) -> Unit,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    var firstInvocationHappen = remember(key) { false }
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it != SwipeToDismissBoxValue.Settled) {
                if (!(firstInvocationHappen)) {
                    firstInvocationHappen = true
                } else {
                    onSwipeFromEndToStartFinished()
                }
            }
            true
        }
    )
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        backgroundContent = {
            backgroundContent(swipeToDismissBoxState)
        },
        modifier = modifier,
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        content = content,
    )
}
