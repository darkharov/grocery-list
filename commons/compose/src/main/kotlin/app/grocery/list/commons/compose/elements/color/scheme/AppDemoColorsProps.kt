package app.grocery.list.commons.compose.elements.color.scheme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
enum class AppDemoColorsSizeProps(
    val size: Dp,
    val offset: Dp,
    val cornerRadius: Dp,
) {
    Compact(
        size = 18.dp,
        offset = 4.dp,
        cornerRadius = 3.dp,
    ),
    Big(
        size = 36.dp,
        offset = 7.dp,
        cornerRadius = 4.dp,
    ),
}
